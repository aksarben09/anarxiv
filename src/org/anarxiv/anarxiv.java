package org.anarxiv;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView;

public class anarxiv extends Activity implements AdapterView.OnItemClickListener, TabHost.OnTabChangeListener
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
	
	/** id of current tab. */
	private String _currentTabId = null;
	
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
	 * check app root dir; create if not exists.
	 */
	public static void checkAppRootDir() throws Exception
	{
		String rootDirPath = ConstantTable.getAppRootDir();
		File rootDir = new File(rootDirPath);
		
		try
		{
			if(rootDir.exists() == false)
				if (rootDir.mkdir() == false)
					throw new Exception("Failed to create application directory at " + rootDirPath);
		}
		catch (SecurityException e)
		{
			throw new Exception(e.getMessage(), e);
		}
	}
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
    	
		/* check app root dir. */
		try
		{
			anarxiv.checkAppRootDir();
		}
		catch(Exception e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
    	
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
        _tabHost.setOnTabChangedListener(this);
        
        /* Category tab. */
        TabHost.TabSpec tabspec = _tabHost.newTabSpec(res.getString(R.string.tabid_Category));
        tabspec.setIndicator(res.getString(R.string.tabstr_Category));
        tabspec.setContent(R.id.categorylist);
        _tabHost.addTab(tabspec);
        
        /* Recent tab. */
        tabspec = _tabHost.newTabSpec(res.getString(R.string.tabid_Recent));
        tabspec.setIndicator(res.getString(R.string.tabstr_Recent));
        tabspec.setContent(R.id.recentlist);
        _tabHost.addTab(tabspec);
        
        /* Favorite tab. */
        tabspec = _tabHost.newTabSpec(res.getString(R.string.tabid_Favorite));
        tabspec.setIndicator(res.getString(R.string.tabstr_Favorite));
        tabspec.setContent(R.id.favlist);
        _tabHost.addTab(tabspec);
        
        /* Fill the category list. */
        _uiCategoryList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, /*UrlTable.Category*/_urlTbl.getMainCategoryList()));
        registerForContextMenu(_uiCategoryList);
	}

    /** 
     * Handler: onItemClick. 
     */
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

	/**
	 * handler: onTabChanged.
	 */
	public void onTabChanged(String tabId) 
	{
		// TODO Auto-generated method stub
		_currentTabId = tabId;
	}
	
	/**
	 * handler: onPrepareOptionsMenu
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		/* tab: category. */
		if (getResources().getString(R.string.tabid_Category).equals(_currentTabId))
		{
			setMenuVisible_Category(menu, true);
			setMenuVisible_Recent(menu, false);
			setMenuVisible_Favorite(menu, false);
		}
		/* tab: recent. */
		else if (getResources().getString(R.string.tabid_Recent).equals(_currentTabId))
		{
			setMenuVisible_Category(menu, false);
			setMenuVisible_Recent(menu, true);
			setMenuVisible_Favorite(menu, false);
		}
		/* tab: favorite. */
		else if (getResources().getString(R.string.tabid_Favorite).equals(_currentTabId))
		{
			setMenuVisible_Category(menu, false);
			setMenuVisible_Recent(menu, false);
			setMenuVisible_Favorite(menu, true);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	/**
	 * handler: onCreateOptionsMenu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}
	
	/**
	 * handler: onOptionsItemSelected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * menu util: tab category.
	 */
	private void setMenuVisible_Category(Menu menu, boolean visible)
	{
		
	}
	
	/**
	 * menu util: tab recent.
	 */
	private void setMenuVisible_Recent(Menu menu, boolean visible)
	{
		MenuItem item = menu.findItem(R.id.mainmenu_recent_category);
		item.setVisible(visible);
		item = menu.findItem(R.id.mainmenu_recent_paper);
		item.setVisible(visible);
	}
	
	/**
	 * menu util: tab favorite.
	 */
	private void setMenuVisible_Favorite(Menu menu, boolean visible)
	{
		MenuItem item = menu.findItem(R.id.mainmenu_fav_category);
		item.setVisible(visible);
		item = menu.findItem(R.id.mainmenu_fav_paper);
		item.setVisible(visible);
	}
}