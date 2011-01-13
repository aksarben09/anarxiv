/**
 * 
 */
package org.anarxiv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

/**
 * @author lihe
 *
 */
public class ArxivLoader 
{
	/**
	 * description of an arxiv paper.
	 */
	public class Paper
	{
		public String _id;
		public String _date;
		public String _title;
		public String _summary;
		public String _url;
		public ArrayList<String> _authors;
		public ArrayList<String> _category;
		public int _fileSize;
	}
	
	/**
	 * loader exception.
	 */
	public class LoaderException extends Exception
	{
		public static final long serialVersionUID = 1L;
		
		LoaderException()
		{
			super();
		}
		
		LoaderException(String msg)
		{
			super(msg);
		}
		
		LoaderException(String msg, Throwable cause)
		{
			super(msg, cause);
		}
	}
	
	/**
	 * current query start point.
	 */
	private int _qStart = 0;
	
	/**
	 * the query url.
	 */
	private String _qUrl = null;
	
	/**
	 * the query category.
	 */
	private String _qCat = null;
	
	/**
	 * max results.
	 */
	private int _maxResults = 10;
	
	/**
	 * convert a paper list to a map list for SimpleAdapter.
	 */
	public static List<Map<String, Object>> toMapList(List<Paper> paperList)
	{
		if (paperList == null)
			return null;
		
		/* conver the paper list to a map list. */
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		for (Paper paper: paperList)
		{
			Map<String, Object> paperMap = new HashMap<String, Object>();
			
			/* get the first author from author list. */
			String author = paper._authors.size() == 1 ? paper._authors.get(0) : paper._authors.get(0) + ", et al";
			
			paperMap.put("date", paper._date);
			paperMap.put("title", paper._title);
			paperMap.put("summary", paper._summary);
			paperMap.put("author", author);
			paperMap.put("authorlist", paper._authors);
			paperMap.put("url", paper._url);
//			paperMap.put("filesize", paper._fileSize);
			
			mapList.add(paperMap);
		}
		
		return mapList;
	}
	
	/**
	 * setter for _maxResults.
	 */
	public void setMaxResults(int maxResults)
	{
		_maxResults = maxResults;
	}
	
	/**
	 * getter for _maxResults.
	 */
	public int getMaxResults()
	{
		return _maxResults;
	}
	
	/**
	 * reset loader.
	 */
	public void reset()
	{
		_qStart = 0;
		_qCat = null;
	}
	
	/**
	 * load paper list from specified url.
	 */
	public ArrayList<Paper> loadPapers(String category) throws LoaderException
	{
		/* invalid query string. */
		if(category == null || category.equals(""))
			throw new LoaderException("Invalid category name.");
		
		/* check if query changed. */
		/* call equals from category since _qCat may be null. */
		if(category.equals(_qCat) == false)
		{
			_qCat = category;
			_qStart = 0;
		}
		
		/* get url. */
		_qUrl = UrlTable.makeQueryUrl(_qCat, _qStart, _maxResults);
		
		/* query the url using URL. */
		Document doc = null;
		
		try
		{
			/* open url and set timeout. */
			URL Url = new URL(_qUrl);
			HttpURLConnection conn = (HttpURLConnection)Url.openConnection();
			conn.setConnectTimeout(ConstantTable.getPaperListLoadTimeout());
			conn.setReadTimeout(ConstantTable.getPaperListLoadTimeout());

			/* prepare xml parser. */
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			/* get input stream. */
			InputStream httpInStream = conn.getInputStream();
			doc = db.parse(httpInStream);
			
			/* parse xml. */
			NodeList entryList = doc.getElementsByTagName("entry");
			
			/* allocate paper list. */
			ArrayList<Paper> paperList = new ArrayList<Paper>();
			
			/* extract paper info. */
			for(int i = 0; i < entryList.getLength(); i ++)
			{
				Element node = (Element)entryList.item(i);
				
				/* get simple tags. */
				String id = node.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
				String title = node.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
				String summary = node.getElementsByTagName("summary").item(0).getFirstChild().getNodeValue();
				String date = node.getElementsByTagName("published").item(0).getFirstChild().getNodeValue();
				
				/* get author list. */
				ArrayList<String> authors = new ArrayList<String>();
				NodeList authorList = node.getElementsByTagName("author");
				for(int j = 0; j < authorList.getLength(); j ++)
				{
					Element authorNode = (Element)authorList.item(j);
					String ath = authorNode.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
					authors.add(ath);
				}
				
				/* get url. */
				String url = ((Element)node.getElementsByTagName("link").item(1)).getAttribute("href");
				
				/* fill in paper structure. */
				Paper entry = new Paper();
				entry._id = id;
				entry._date = date.replace('T',	' ').replace('Z', ' ');
				entry._title = title.replace("\n ", " ");
				entry._summary = summary.replace('\n', ' ').replace("  ", "\n  ").substring(1);
				entry._authors = authors;
				entry._url = url;
//				entry._fileSize = ArxivFileDownloader.getFileSize(url);
				
				paperList.add(entry);
			}
			
			/* increase starting point. */
			_qStart += _maxResults;
			
			return paperList;
		}
		catch(MalformedURLException e)
		{
			throw new LoaderException(e.getMessage(), e);
		}
		catch(SocketTimeoutException e)
		{
			throw new LoaderException(e.getMessage(), e);
		}
		catch(IOException e)
		{
			throw new LoaderException(e.getMessage(), e);
		}
		catch(SAXException e)
		{
			throw new LoaderException("Bad data received, possibly bad connection.", e);
		}
		catch(Exception e)
		{
			throw new LoaderException(e.getMessage());
		}
	}
}
