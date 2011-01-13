/**
 * 
 */
package org.anarxiv;

import java.io.File;

import android.os.Environment;

/**
 * @author lihe
 * 
 * Utilities for manage storages.
 */
public class StorageUtils 
{
	/**
	 * check if external storage is available.
	 */
	public static boolean isExternalStorageAvailable()
	{
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED.equals(state) == true)
			return true;
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) == true)
			return true;
		
		return false;
	}
	
	/**
	 * check if external storage is read-only.
	 */
	public static boolean isExternalStorageReadOnly()
	{
		String state = Environment.getExternalStorageState();
		
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) == true)
			return true;
		return false;
	}
	
	/**
	 * get external storage root.
	 */
	public static String getExternalStorageRoot()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * remove a file
	 */
	public static boolean removeFile(String filePath) throws SecurityException
	{
		try
		{
			File file = new File(filePath);
			return file.delete();
		}
		catch (SecurityException e)
		{
			throw e;
		}
	}
}
