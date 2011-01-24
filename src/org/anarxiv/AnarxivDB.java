/**
 * 
 */
package org.anarxiv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;

/**
 * @author lihe
 *
 */
public class AnarxivDB
{
	/**
	 * paper information.
	 */
	public static class Paper
	{
		public String _id;
		public String _date;
		public String _title;
		public String _author;
		public String _url;
	}
	
	/**
	 * category information.
	 */
	public static class Category
	{
		public String _name;
		public String _queryWord;
	}
	
	/**
	 * database exception.
	 */
	public static class DBException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		DBException()
		{
			
		}
		
		DBException(String msg)
		{
			super(msg);
		}
		
		DBException(String msg, Throwable e)
		{
			super(msg, e);
		}
	}
	
	/**
	 * database version.
	 */
	private static final int _databaseVersion = 2;
	
	/**
	 * database name.
	 */
	private static final String _databasePath = "anarxivdb";
	
	/**
	 * table names.
	 */
	private static final String _tbl_RecentPaper = "recent_paper";
	private static final String _tbl_FavoritePaper = "favorite_paper";
	
	/**
	 * table creation statements.
	 */
	private static final String _createTbl_RecentPaper = 
							"create table if not exists " + AnarxivDB._tbl_RecentPaper + 
							"(db_id integer primary key autoincrement, " +
							"_date text, " +
							"_id text, " +
							"_title text, " +
							"_author text, " +
							"_url text)";
	private static final String _createTbl_FavoritePaper = 
							"create table if not exists " + AnarxivDB._tbl_FavoritePaper + 
							"(db_id integer primary key autoincrement, " +
							"_date text, " +
							"_id text, " +
							"_title text, " +
							"_author text, " +
							"_url text)";
	
	/**
	 * the sqlite database object.
	 */
	private SQLiteDatabase _sqliteDB = null;
	
	/**
	 * singleton instance.
	 */
	private static AnarxivDB _theInstance = null;
	
	/**
	 * context.
	 */
	private static Context _context = null;
	
	/**
	 * get the instance.
	 */
	public static AnarxivDB getInstance()
	{
		if (AnarxivDB._theInstance == null)
			_theInstance = new AnarxivDB();
		return AnarxivDB._theInstance;
	}
	
	/**
	 * set owner context. must be set before use.
	 */
	public static void setOwner(Context context)
	{
		AnarxivDB._context = context;
	}

	/**
	 * 
	 */
	public AnarxivDB() 
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * util: data converter.
	 */
	public static ContentValues paperToContentValues(Paper paper)
	{
		ContentValues cv = new ContentValues();
		cv.put("_id", paper._id);
		cv.put("_date", paper._date);
		cv.put("_title", paper._title);
		cv.put("_url", paper._url);
		cv.put("_author", paper._author);
		return cv;
	}
	
	/**
	 * util: data converter.
	 */
	public static List<HashMap<String, Object>> paperListToMapList(List<Paper> paperList)
	{
		List<HashMap<String, Object>> mapList = new ArrayList<HashMap<String, Object>>();
		
		for (Paper paper: paperList)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("author", paper._author);
			map.put("date", paper._date);
			map.put("id", paper._id);
			map.put("title", paper._title);
			map.put("url", paper._url);
			
			mapList.add(map);
		}
		
		return mapList;
	}
	
	/**
	 * util: data converter.
	 */
	public static ContentValues categoryTpContentValues(Category category)
	{
		ContentValues cv = new ContentValues();
		cv.put("_name", category._name);
		cv.put("_queryword", category._queryWord);
		return cv;
	}
	
	/**
	 * open the database.
	 */
	public void open() throws DBException
	{
		try
		{
			if (_sqliteDB == null)
			{
				/* open database. */
				_sqliteDB = _context.openOrCreateDatabase(AnarxivDB._databasePath, 
														  Context.MODE_WORLD_WRITEABLE,
														  null);
				
				/* create tables. */
				_sqliteDB.execSQL(AnarxivDB._createTbl_RecentPaper);
				_sqliteDB.execSQL(AnarxivDB._createTbl_FavoritePaper);
			}
		}
		catch (SQLiteException e)
		{
			throw new DBException(e.getMessage(), e);
		}
	}
	
	/**
	 * close the database.
	 */
	public void close()
	{
		_sqliteDB.close();
	}

	/**
	 * insert a recent paper.
	 */
	public long addRecentPaper(Paper paper) throws DBException
	{
		try
		{
			return _sqliteDB.insert(AnarxivDB._tbl_RecentPaper, null, AnarxivDB.paperToContentValues(paper));
		}
		catch (SQLiteException e)
		{
			throw new DBException(e.getMessage(), e);
		}
	}
	
	/**
	 * insert a recent category.
	 */
	public long addRecentCategory(Category category) throws DBException
	{
		return 0;
	}
	
	/**
	 * insert a favorite paper.
	 */
	public long addFavoritePaper(Paper paper) throws DBException
	{
		try
		{
			return _sqliteDB.insert(AnarxivDB._tbl_FavoritePaper, null, AnarxivDB.paperToContentValues(paper));
		}
		catch (SQLiteException e)
		{
			throw new DBException(e.getMessage(), e);
		}
	}
	
	/**
	 * insert a favorite category.
	 */
	public long addFavoriteCategory(Category category) throws DBException
	{
		return 0;
	}
	
	/**
	 * get favorite paper list.
	 */
	public ArrayList<Paper> getFavoritePapers() throws DBException
	{
		return null;
	}
	
	/**
	 * get most recent papers.
	 */
	public ArrayList<Paper> getRecentPapers(Integer limit) throws DBException
	{
		try
		{
			/* query the database. */
			Cursor c = _sqliteDB.query(AnarxivDB._tbl_RecentPaper, 
									   new String[] {"_id", "_author", "_title", "_date", "_url"}, 
									   null, 
									   null, 
									   null, 
									   null, 
									   "db_id desc",
									   limit < 0 ? null : limit.toString());
			c.moveToFirst();
			
			ArrayList<Paper> paperList = new ArrayList<Paper>();
			
			for (int i = 0; i < c.getCount(); i ++)
			{
				Paper paper = new Paper();
				paper._author = c.getString(c.getColumnIndex("_author"));
				paper._date = c.getString(c.getColumnIndex("_date"));
				paper._id = c.getString(c.getColumnIndex("_id"));
				paper._title = c.getString(c.getColumnIndex("_title"));
				paper._url = c.getString(c.getColumnIndex("_url"));
				
				paperList.add(paper);
				c.moveToNext();
			}
			
			return paperList;
		}
		catch (SQLiteException e)
		{
			throw new DBException(e.getMessage(), e);
		}
	}
	
	/**
	 * get favorite category list.
	 */
	public ArrayList<Category> getFavoriteCategories() throws DBException
	{
		return null;
	}
	
	/**
	 * get recent category list.
	 */
	public ArrayList<Category> getRecentCategories() throws DBException
	{
		return null;
	}
}
