/**
 * 
 */
package org.anarxiv;

import java.net.URL;
import java.io.File;

/**
 * @author lihe
 *
 * class for downloading pdf files from arxiv.
 */
public class ArxivFileDownloader 
{
	/**
	 * exception.
	 */
	public class FileDownloaderException extends Exception
	{
		/**
		 * 
		 */
		FileDownloaderException()
		{
			super();
		}
		
		/**
		 * 
		 */
		FileDownloaderException(String msg)
		{
			super(msg);
		}
		
		/**
		 * 
		 */
		FileDownloaderException(String msg, Throwable cause)
		{
			super(msg, cause);
		}
	}
	
	/**
	 * download a file.
	 */
	public void download(String url, File localTarget)
	{
		
	}
	
	/**
	 * download a file.
	 */
	public void download(URL url, File localTarget)
	{
		
	}
}
