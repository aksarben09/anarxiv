/**
 * 
 */
package org.anarxiv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


/**
 * @author lihe
 *
 */
public class PaperListWnd extends Activity implements OnItemClickListener, OnScrollListener
{
	/** ui components. */
	ListView _uiPaperList = null;
	TextView _uiCategoryName = null;
	ProgressDialog _uiBusyBox = null;
	
	/** adapter for paper list. */
	SimpleAdapter _uiPaperListAdapter = null;
	
	/** gesture detector. */
	private GestureDetector _gestureDetector = null;
	
	/** sync lock. */
//	private Object _lock = new Object();
	
	/** whether loading thread is running. */
	private boolean _isLoading = false;
	
	/** arxiv loader. */
	private ArxivLoader _arxivLoader = anarxiv.getArxivLoaderInstance();
	
	/** the category of this list for query. */
	private String _paperCategory = null;
	
	/** the descriptive name of the category. */
	private String _paperCategoryName = null;
	
	/** newly loaded paper list. */
//	private ArrayList<ArxivLoader.Paper> _newPaperList = null;
	
	/** paper map list. */
	private List<Map<String, Object>> _paperMapList = new ArrayList<Map<String, Object>>();
	
	/**
	 * gesture handler.
	 */
	private class myGestureListener extends GestureDetector.SimpleOnGestureListener
	{
		/**
		 * onFling.
		 */
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			if (e1.getX() - e2.getX() > ConstantTable.FLING_MIN_DISTANCE && 
					Math.abs(velocityX) > ConstantTable.FLING_MIN_VELOCITY)
			{
				finish();
			}
			
			return super.onFling(e1, e2, velocityX, velocityY);
		}
	}
	
	/**
	 * Loading thread.
	 */
	private class ArxivLoadingThread extends Thread
	{
		public synchronized void run()
		{
			/* get mainlooper. */
			Looper mainLooper = Looper.getMainLooper();
			
			try
			{	
				/* get data. */
			//	ArrayList<ArxivLoader.Paper> newPaperList = _arxivLoader.loadPapers(_paperCategory);
				List<Map<String, Object>> paperMapList = _arxivLoader.loadPapers(_paperCategory);
				_paperMapList.addAll(paperMapList);
					
				/* send message. */
				PaperDataHandler handler = new PaperDataHandler(mainLooper);
				handler.removeMessages(0);
				handler.sendEmptyMessage(0);
			}
			catch(ArxivLoader.LoaderException e)
			{
//				new AlertDialog.Builder(PaperListWnd.this).setTitle(R.string.error_dialog_title)
//							   .setMessage(e.getMessage())
//							   .setPositiveButton(R.string.confirm_btn_caption, null)
//							   .show();
				
				ExceptionHandler handler = new ExceptionHandler(mainLooper);
				Message msg = handler.obtainMessage(0, e.getMessage());
				handler.removeMessages(0);
				handler.sendMessage(msg);
			}
		}
	}
	
	/**
	 * handler for handling paper data.
	 */
	private class PaperDataHandler extends Handler
	{
		/**
		 * 
		 */
		public PaperDataHandler(Looper looper)
		{
			super(looper);
		}
		
		/**
		 * handler.
		 */
		@Override
		public void handleMessage(Message msg)
		{	
			if (_uiPaperListAdapter == null)
			{
				_uiPaperListAdapter = new SimpleAdapter(PaperListWnd.this,
														_paperMapList,
														R.layout.paperlistitem,
														new String[] {"title", 
																	  "date", 
																	  "author"},
														new int[] {R.id.paperitem_title, 
																   R.id.paperitem_date, 
																   R.id.paperitem_author});
				_uiPaperList.setAdapter(_uiPaperListAdapter);
			}
			else
			{
				/* notify the view that the data has changed. */
				_uiPaperListAdapter.notifyDataSetChanged();
			}
			
			if (_uiBusyBox != null)
			{
				_uiBusyBox.dismiss();
				_uiBusyBox = null;
			}
			
			_isLoading = false;
		}
	}
	
	/**
	 * error handler.
	 */
	private class ExceptionHandler extends Handler
	{
		/**
		 * 
		 */
		public ExceptionHandler(Looper looper)
		{
			super(looper);
		}
		
		/**
		 * handler.
		 */
		@Override
		public void handleMessage(Message msg)
		{
			/* dismiss busy box if any. */
			if (_uiBusyBox != null)
			{
				_uiBusyBox.dismiss();
				_uiBusyBox = null;
			}
			
			_isLoading = false;
			
			/* show error message. */
			UiUtils.showErrorMessage(PaperListWnd.this, (String)msg.obj);
		}
	}
	
	/** 
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paperlist);
		
		/* reste loader. */
		_arxivLoader.reset();
		
		/* get intent params. */
		Intent intent = getIntent();
		_paperCategory = intent.getStringExtra("category");
		_paperCategoryName = intent.getStringExtra("categoryname");
		
		/* get ui components. */
		_uiPaperList = (ListView)findViewById(R.id.paperlist);
		_uiCategoryName = (TextView)findViewById(R.id.categoryname);
		
		/* set ui category name. */
		_uiCategoryName.setText(_paperCategoryName);
		
		/* set event handler. */
		_uiPaperList.setOnItemClickListener(this);
		_uiPaperList.setOnScrollListener(this);
		
		/* register context menu. */
		registerForContextMenu(_uiPaperList);
		
		/* gesture detector. */
		_gestureDetector = new GestureDetector(this, new myGestureListener());
		
		/* show busy box. */
		_uiBusyBox = ProgressDialog.show(this, "",
										 getResources().getText(R.string.loading_please_wait));
		
		ArxivLoadingThread t = new ArxivLoadingThread();
		t.start();
	}
	
	/**
	 * intercept all touch event for gesture detector.
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		_gestureDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}
	
	/**
	 * onItemClick.
	 */
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>)a.getItemAtPosition(position);
		
		try
		{
			AnarxivDB db = AnarxivDB.getInstance();
			
			/* fill out the paper object and add to database. */
			AnarxivDB.Paper paper = new AnarxivDB.Paper();
			paper._author = (String)item.get("author");
			paper._date = (String)item.get("date");
			paper._id = (String)item.get("id");
			paper._title = (String)item.get("title");
			paper._url = (String)item.get("url");
			
			db.addRecentPaper(paper);
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
		
		Intent intent = new Intent(this, PaperDetailWnd.class);
		intent.putExtra("paperdetail", item);
		startActivity(intent);
	}

	/**
	 * onScroll.
	 */
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) 
	{
		// TODO Auto-generated method stub
		if(totalItemCount <= 0)
		{
			return;
		}
		
		if (firstVisibleItem + visibleItemCount >= _paperMapList.size() && _isLoading == false)
		{
			try
			{
				ArxivLoadingThread t = new ArxivLoadingThread();
				t.start();
					
				/* show busy box. */
				_uiBusyBox = ProgressDialog.show(this, 
												 "",
												 getResources().
												 	getText(R.string.loading_please_wait));
			}
			catch(IllegalThreadStateException e)
			{
				return;
			}
			
			_isLoading = true;
		}
	}

	/**
	 * onScrollStateChange.
	 */
	public void onScrollStateChanged(AbsListView view, int scrollState) 
	{
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * override: context menu.
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ctxmenu_add_to_favorite, menu);
		menu.setHeaderTitle("Add to Favorite");
	}
	
	/**
	 * override: context menu handler.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		if (item.getItemId() == R.id.ctxmenu_add_to_favorite)
		{
			@SuppressWarnings("unchecked")
			HashMap<String, Object> itemData = (HashMap<String, Object>)_uiPaperList.getItemAtPosition(info.position);
			
			AnarxivDB.Paper paper = new AnarxivDB.Paper();
			paper._author = (String)itemData.get("author");
			paper._date = (String)itemData.get("date");
			paper._id = (String)itemData.get("id");
			paper._title = (String)itemData.get("title");
			paper._url = (String)itemData.get("url");
			
			try
			{
				AnarxivDB.getInstance().addFavoritePaper(paper);
				UiUtils.showToast(this, "Added to favorite: " + paper._title);
			}
			catch (AnarxivDB.DBException e)
			{
				UiUtils.showToast(this, e.getMessage());
			}
		}
		
		return super.onContextItemSelected(item);
	}
}
