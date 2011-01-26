package org.anarxiv;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

public class anarxiv extends Activity implements AdapterView.OnItemClickListener, TabHost.OnTabChangeListener
{
	/** UI components. */
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
	
	/** ui states. */
	private int _RecentTabState = R.id.mainmenu_recent_category;
	private int _FavoriteTabState = R.id.mainmenu_favorite_category;
	
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
    	
		try
		{
			/* check app root dir. */
			anarxiv.checkAppRootDir();
			
			/* open the database. */
			AnarxivDB.setOwner(this);
			AnarxivDB.getInstance().open();
		}
		catch(Exception e)
		{
			UiUtils.showErrorMessage(this, e.getMessage());
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
        registerForContextMenu(_uiFavoriteList);
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
		
		/* tab recent is clicked. */
		if (getResources().getString(R.string.tabid_Recent).equals(_currentTabId))
		{
			if (_RecentTabState == R.id.mainmenu_recent_category)
				loadRecentCategories();
			else if (_RecentTabState == R.id.mainmenu_recent_paper)
				loadRecentPapers();
		}
		else if (getResources().getString(R.string.tabid_Favorite).equals(_currentTabId))
		{
			if (_FavoriteTabState == R.id.mainmenu_favorite_category)
				loadFavoriteCategories();
			else if (_FavoriteTabState == R.id.mainmenu_favorite_paper)
				loadFavoritePapers();
		}
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
		/* recent paper. */
		if (item.getItemId() == R.id.mainmenu_recent_paper)
		{
			_RecentTabState = item.getItemId();
			loadRecentPapers();
		}
		/* remove all recent history. */
		else if (item.getItemId() == R.id.mainmenu_recent_delete_all)
		{
			removeAllRecentPapers();
			removeAllRecentCategories();
			loadRecentCategories();
		}
		/* recent category. */
		else if (item.getItemId() == R.id.mainmenu_recent_category)
		{
			_RecentTabState = item.getItemId();
			loadRecentCategories();
		}
		/* favorite paper. */
		else if (item.getItemId() == R.id.mainmenu_favorite_paper)
		{
			_FavoriteTabState = item.getItemId();
			loadFavoritePapers();
		}
		/* remove all favorite items. */
		else if (item.getItemId() == R.id.mainmenu_favorite_delete_all)
		{
			removeFavoritePaper(null);
			removeFavoriteCategory(null);
			loadFavoriteCategories();
		}
		/* favorite category. */
		else if (item.getItemId() == R.id.mainmenu_favorite_category)
		{
			_FavoriteTabState = item.getItemId();
			loadFavoriteCategories();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * override: context menu.
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ctxmenu_delete_from_favorite, menu);
		menu.setHeaderTitle("Delete");
	}
	
	/**
	 * override: context menu handler.
	 */
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		if (item.getItemId() == R.id.ctxmenu_delete_from_favorite)
		{
			HashMap<String, Object> itemData = (HashMap<String, Object>)_uiFavoriteList.getItemAtPosition(info.position);
			
			if (_FavoriteTabState == R.id.mainmenu_favorite_category)
			{
				AnarxivDB.Category category = new AnarxivDB.Category();
				category._name = (String)itemData.get("name");
				category._parent = (String)itemData.get("parent");
				category._queryWord = (String)itemData.get("queryword");
				
				try
				{
					AnarxivDB.getInstance().removeFavoriteCategory(category);
					UiUtils.showToast(this, "Deleted: " + category._name);
					loadFavoriteCategories();
				}
				catch (AnarxivDB.DBException e)
				{
					UiUtils.showToast(this, e.getMessage());
				}
			}
			else if (_FavoriteTabState == R.id.mainmenu_favorite_paper)
			{
				AnarxivDB.Paper paper = new AnarxivDB.Paper();
				paper._author = (String)itemData.get("author");
				paper._date = (String)itemData.get("date");
				paper._id = (String)itemData.get("id");
				paper._title = (String)itemData.get("title");
				paper._url = (String)itemData.get("url");
				
				try
				{
					AnarxivDB.getInstance().removeFavoritePaper(paper);
					UiUtils.showToast(this, "Deleted: " + paper._title);
					loadFavoritePapers();
				}
				catch (AnarxivDB.DBException e)
				{
					UiUtils.showToast(this, e.getMessage());
				}
			}
		}
		
		return super.onContextItemSelected(item);
	}
	
	/**
	 * load recent papers from database.
	 */
	private void loadRecentPapers()
	{
		try
		{
			/* display recently access paper. */
			AnarxivDB db = AnarxivDB.getInstance();
			List<HashMap<String, Object>> recentPaperList = AnarxivDB.paperListToMapList(db.getRecentPapers(-1));
			
			/* adapter. */
			SimpleAdapter adapter = new SimpleAdapter(this,
													  recentPaperList,
													  R.layout.paperlistitem,
													  new String[] {"title",
																	"date",
																	"author"},
													  new int[] {R.id.paperitem_title, 
																 R.id.paperitem_date, 
																 R.id.paperitem_author});
			this._uiRecentList.setAdapter(adapter);
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
	}
	
	/**
	 * load recent categories.
	 */
	private void loadRecentCategories()
	{
		try
		{
			AnarxivDB db = AnarxivDB.getInstance();
			List<HashMap<String, Object>> recentCategoryList = AnarxivDB.categoryListToMapList(db.getRecentCategories());
			
			/* adapter. */
			SimpleAdapter adapter = new SimpleAdapter(this,
													  recentCategoryList,
													  R.layout.recent_category_list_item,
													  new String[] {"name", "parent"},
													  new int[] {R.id.recent_category_list_name, R.id.recent_category_list_parent});
			this._uiRecentList.setAdapter(adapter);
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
	}
	
	/**
	 * load favorite papers.
	 */
	private void loadFavoritePapers()
	{
		try
		{
			AnarxivDB db = AnarxivDB.getInstance();
			List<HashMap<String, Object>> favoritePaperList = AnarxivDB.paperListToMapList(db.getFavoritePapers());
			
			/* adapter. */
			SimpleAdapter adapter = new SimpleAdapter(this,
													  favoritePaperList,
													  R.layout.paperlistitem,
													  new String[] {"title",
																	"date",
																	"author"},
													  new int[] {R.id.paperitem_title, 
																 R.id.paperitem_date, 
																 R.id.paperitem_author});
			this._uiFavoriteList.setAdapter(adapter);
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
	}
	
	/**
	 * load favorite categories.
	 */
	private void loadFavoriteCategories()
	{
		try
		{
			AnarxivDB db = AnarxivDB.getInstance();
			List<HashMap<String, Object>> favoriteCategoryList = AnarxivDB.categoryListToMapList(db.getFavoriteCategories());
			
			/* adapter. */
			SimpleAdapter adapter = new SimpleAdapter(this,
													  favoriteCategoryList,
													  R.layout.recent_category_list_item,
													  new String[] {"name", "parent"},
													  new int[] {R.id.recent_category_list_name, R.id.recent_category_list_parent});
			this._uiFavoriteList.setAdapter(adapter);
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
	}
	
	/**
	 * remove all recent papers.
	 */
	private void removeAllRecentPapers()
	{
		try
		{
			AnarxivDB.getInstance().removeAllRecentPapers();
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
	}
	
	/**
	 * remove all recent categories.
	 */
	private void removeAllRecentCategories()
	{
		try
		{
			AnarxivDB.getInstance().removeAllRecentCategories();
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
	}
	
	/**
	 * remove favorite paper.
	 */
	private void removeFavoritePaper(AnarxivDB.Paper paper)
	{
		try
		{
			AnarxivDB.getInstance().removeFavoritePaper(paper);
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
	}
	
	/**
	 * remove favorite category.
	 */
	private void removeFavoriteCategory(AnarxivDB.Category category)
	{
		try
		{
			AnarxivDB.getInstance().removeFavoriteCategory(category);
		}
		catch (AnarxivDB.DBException e)
		{
			UiUtils.showToast(this, e.getMessage());
		}
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
		item = menu.findItem(R.id.mainmenu_recent_delete_all);
		item.setVisible(visible);
	}
	
	/**
	 * menu util: tab favorite.
	 */
	private void setMenuVisible_Favorite(Menu menu, boolean visible)
	{
		MenuItem item = menu.findItem(R.id.mainmenu_favorite_category);
		item.setVisible(visible);
		item = menu.findItem(R.id.mainmenu_favorite_paper);
		item.setVisible(visible);
		item = menu.findItem(R.id.mainmenu_favorite_delete_all);
		item.setVisible(visible);
	}
}