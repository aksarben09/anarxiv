/**
 * 
 */
package org.anarxiv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
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
				ArrayList<ArxivLoader.Paper> newPaperList = _arxivLoader.loadPapers(_paperCategory);
				List<Map<String, Object>> paperMapList = ArxivLoader.toMapList(newPaperList);
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
		
		/* show busy box. */
		_uiBusyBox = ProgressDialog.show(this, "",
										 getResources().getText(R.string.loading_please_wait));
		
		ArxivLoadingThread t = new ArxivLoadingThread();
		t.start();
	}
	
	/**
	 * onItemClick.
	 */
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>)a.getItemAtPosition(position);
		
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
}
