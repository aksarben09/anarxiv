package org.anarxiv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView;

public class anarxiv extends Activity implements AdapterView.OnItemClickListener
{
	/* UI components. */
	private TabHost _tabHost = null;
	
	private ListView _uiCategoryList = null;
	private ListView _uiRecentList = null;
	private ListView _uiFavoriteList = null;
	
	/** Url table. */
	public static final UrlTable _urlTbl = new UrlTable();
	
	/** arxiv loader. */
	private static ArxivLoader _arxivLoader = null;
	
	/**
	 * get ArxivLoader instance.
	 */
	public static ArxivLoader getArxivLoaderInstance()
	{
		if(anarxiv._arxivLoader == null)
			anarxiv._arxivLoader = new ArxivLoader();
		return anarxiv._arxivLoader;
	}
	
	/**
	 * show an error box.
	 */
	public static void showErrorMessage(Context context, String errMsg)
	{
		new AlertDialog.Builder(context)
					   .setTitle(R.string.error_dialog_title)
					   .setMessage(errMsg)
					   .setPositiveButton(R.string.confirm_btn_caption, null)
					   .show();
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /* get resource manager. */
        Resources res = getResources();
        
        /* get ui components. */
        _uiCategoryList = (ListView)findViewById(R.id.categorylist);
        _uiRecentList = (ListView)findViewById(R.id.recentlist);
        _uiFavoriteList = (ListView)findViewById(R.id.favlist);
        
        /* event handler. */
        _uiCategoryList.setOnItemClickListener(this);
        
        /* Tab host setup. */
        _tabHost = (TabHost)findViewById(R.id.tabhost);
        _tabHost.setup();
        
        /* Category tab. */
        TabHost.TabSpec tabspec = _tabHost.newTabSpec("category");
        tabspec.setIndicator(res.getString(R.string.tabstr_Category));
        tabspec.setContent(R.id.categorylist);
        _tabHost.addTab(tabspec);
        
        /* Recent tab. */
        tabspec = _tabHost.newTabSpec("recent");
        tabspec.setIndicator(res.getString(R.string.tabstr_Recent));
        tabspec.setContent(R.id.recentlist);
        _tabHost.addTab(tabspec);
        
        /* Favorite tab. */
        tabspec = _tabHost.newTabSpec("fav");
        tabspec.setIndicator(res.getString(R.string.tabstr_Favorite));
        tabspec.setContent(R.id.favlist);
        _tabHost.addTab(tabspec);
        
        /* Fill the category list. */
        _uiCategoryList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, /*UrlTable.Category*/_urlTbl.getMainCategoryList()));
        registerForContextMenu(_uiCategoryList);
    }

    /** Handler: onItemClick. */
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		/* category clicked. */
		if(a.getId() == R.id.categorylist)
		{
			String mainCatItem = (String)a.getItemAtPosition(position);
			String[] subCatList = _urlTbl.getSubcategoryList(mainCatItem);
			
			Intent intent = new Intent(this, SubCategoryWnd.class);
			intent.putExtra("subcatname", mainCatItem);
			intent.putExtra("subcatlist", subCatList);
			startActivity(intent);
		}
		/* recent  clicked.*/
		else if(a.getId() == R.id.recentlist)
		{
			
		}
		/* favorite clicked. */
		else if(a.getId() == R.id.favlist)
		{
			
		}
	}
}