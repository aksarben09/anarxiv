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
}
