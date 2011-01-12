/**
 * 
 */
package org.anarxiv;

import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author lihe
 *
 */
public class PaperDetailWnd extends Activity 
{
	/**
	 * ui components.
	 */
	private ListView _uiAuthorList = null;
	private TextView _uiPaperTitle = null;
	private TextView _uiSummary = null;
	private TextView _uiPaperDate = null;
	
	/**
	 * util: view pdf file.
	 */
	private void viewPdf(File pdfFile)
	{
		if(pdfFile.exists() == false)
		{
			UIUtils.showErrorMessage(this, "File does not exist: " + pdfFile.getName());
			return;
		}
		
		/* get file uri. */
		Uri path = Uri.fromFile(pdfFile);
		
		/* create intent and launch activity. */
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(path, "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		try
		{
			startActivity(intent);
		}
		catch(ActivityNotFoundException e)
		{
			Toast.makeText(this, 
						   this.getResources().getText(R.string.error_no_pdf_viewer), 
						   Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * thread for downloading pdf from arxiv.
	 */
	private class PdfDownloadingThread extends Thread
	{
		/**
		 * 
		 */
		@Override
		public void run()
		{
			
		}
	}
	
	/**
	 * handler for when pdf download finishes.
	 */
	private class PdfHandler extends Handler
	{
		/**
		 * 
		 */
		PdfHandler(Looper looper)
		{
			super(looper);
		}
		
		/**
		 * handler.
		 */
		@Override
		public void handleMessage(Message msg)
		{
			
		}
	}
	
	/**
	 * handler for pdf download exception.
	 */
	private class PdfDownloadExceptionHandler extends Handler
	{
		/**
		 * 
		 */
		PdfDownloadExceptionHandler(Looper looper)
		{
			super(looper);
		}
		
		/**
		 * handler.
		 */
		@Override
		public void handleMessage(Message msg)
		{
			
		}
	}
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paperdetailview);
		
		/* get intent and associated params. */
		Intent intent = getIntent();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> detail = (HashMap<String, Object>)intent.getSerializableExtra("paperdetail");
		
		/* get ui components. */
		_uiAuthorList = (ListView)findViewById(R.id.paperdetail_authorlist);
		_uiPaperTitle = (TextView)findViewById(R.id.paperdetail_title);
		_uiSummary = (TextView)findViewById(R.id.paperdetail_summary);
		_uiPaperDate = (TextView)findViewById(R.id.paperdetail_date);
		
		/* set text data. */
		_uiPaperTitle.setText((String)detail.get("title"));
		_uiPaperDate.setText((String)detail.get("date"));
		_uiSummary.setText((String)detail.get("summary"));
		
		/* set list data. */
		@SuppressWarnings("unchecked")
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
																R.layout.authorlistitem,
																(ArrayList<String>)detail.get("authorlist"));
		_uiAuthorList.setAdapter(adapter);
	}
	
	/**
	 * create options menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_paperdetail, menu);
		return true;
	}
	
	/**
	 * handle options menu.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_paperdetail_view)
		{
			
		}
		
		return super.onOptionsItemSelected(item);
	}
}
