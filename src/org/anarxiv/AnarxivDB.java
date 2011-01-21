/**
 * 
 */
package org.anarxiv;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
		public String _summary;
		public String _author;
		public String _url;
		public ArrayList<String> _authorlist;
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
	 * singleton instance.
	 */
	private static AnarxivDB _theInstance = null;
	
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
	 * 
	 */
	public AnarxivDB() 
	{
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * util: data converter.
	 */
	private ContentValues paperToContentValues(Paper paper)
	{
		return null;
	}
	
	/**
	 * util: data converter.
	 */
	private ContentValues categoryTpContentValues(Category category)
	{
		return null;
	}
	
	/**
	 * open the database.
	 */
	public void open() throws DBException
	{
		
	}

	/**
	 * insert a recent paper.
	 */
	public void addRecentPaper(Paper paper) throws DBException
	{
		
	}
	
	/**
	 * insert a recent category.
	 */
	public void addRecentCategory(Category category) throws DBException
	{
		
	}
	
	/**
	 * insert a favorite paper.
	 */
	public void addFavoritePaper(Paper paper) throws DBException
	{
		
	}
	
	/**
	 * insert a favorite category.
	 */
	public void addFavoriteCategory(Category category) throws DBException
	{
		
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
	public ArrayList<Paper> getRecentPapers() throws DBException
	{
		return null;
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
