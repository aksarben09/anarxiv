/**
 * 
 */
package org.anarxiv;

import java.util.HashMap;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


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
}
