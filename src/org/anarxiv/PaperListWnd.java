/**
 * 
 */
package org.anarxiv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


/**
 * @author lihe
 *
 */
public class PaperListWnd extends Activity implements OnItemClickListener
{
	/** ui components. */
	ListView _uiPaperList = null;
	TextView _uiCategoryName = null;
	
	/** arxiv loader. */
	private ArxivLoader _arxivLoader = anarxiv.getArxivLoaderInstance();
	
	/** the category of this list for query. */
	private String _paperCategory = null;
	
	/** the descriptive name of the category. */
	private String _paperCategoryName = null;
	
	/** 
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paperlist);
		
		/* get intent params. */
		Intent intent = getIntent();
		_paperCategory = intent.getStringExtra("category");
		_paperCategoryName = intent.getStringExtra("categoryname");
		
		/* get ui components. */
		_uiPaperList = (ListView)findViewById(R.id.paperlist);
		_uiCategoryName = (TextView)findViewById(R.id.categoryname);
		
		/* set ui category name. */
		_uiCategoryName.setText(_paperCategoryName);
		
		/* load data. */
		try
		{
			ArrayList<ArxivLoader.Paper> paperList = _arxivLoader.loadPapers(_paperCategory);
			List<Map<String, Object>> paperMapList = ArxivLoader.toMapList(paperList);
			
			SimpleAdapter adapter = new SimpleAdapter(this,
													  paperMapList,
													  R.layout.paperlistitem,
													  new String[] {"title", 
																	"date", 
																	"author"},
													  new int[] {R.id.paperitem_title, 
																 R.id.paperitem_date, 
																 R.id.paperitem_author});
			_uiPaperList.setAdapter(adapter);
		}
		catch(ArxivLoader.LoaderException e)
		{
			new AlertDialog.Builder(this).setTitle(R.string.error_dialog_title)
										 .setMessage(e.getMessage())
										 .setPositiveButton(R.string.confirm_btn_caption, null)
										 .show();
		}
	}
	
	/**
	 * event handler
	 */
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		// TODO Auto-generated method stub
		
	}
}
