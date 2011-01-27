/**
 * 
 */
package org.anarxiv;

/**
 * @author lihe
 *
 */
public class ConstantTable 
{
	/** timeout for loading paper list. */
	private static int _timeoutPaperListLoad = 5000;
	
	/** consts for fling. */
	public static final int FLING_MIN_DISTANCE = 100;
	public static final int FLING_MIN_VELOCITY = 25;
	
	/**
	 * set paper list loading timeout.
	 */
	public static void setPaperListLoadTimeout(int timeout)
	{
		_timeoutPaperListLoad = timeout;
	}
	
	/**
	 * get paper list loading timeout.
	 */
	public static int getPaperListLoadTimeout()
	{
		return _timeoutPaperListLoad;
	}
	
	/**
	 * get application root dir.
	 */
	public static String getAppRootDir()
	{
		return StorageUtils.getExternalStorageRoot() + "/aNarXiv";
	}
}
