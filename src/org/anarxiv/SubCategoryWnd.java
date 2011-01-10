package org.anarxiv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.AlertDialog;


/**
 * @author lihe
 *
 */
public class SubCategoryWnd extends Activity implements OnItemClickListener
{
	/* ui components. */
	private ListView _uiSubCatList = null;
	private TextView _uiSubCatName = null;
	
	/** sub category name. */
	private String _subCatName = null;
	
	/** sub category list. */
	private String[] _subCatList = null;
	
	/** arxiv loader. */
	private ArxivLoader _arxivLoader = anarxiv.getArxivLoaderInstance();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subcategory);
		
		/* get parameters from intent. */
		Intent intent = getIntent();
		_subCatName = intent.getStringExtra("subcatname");
		_subCatList = intent.getStringArrayExtra("subcatlist");
		
		/* get ui. */
		_uiSubCatList = (ListView)this.findViewById(R.id.subcategorylist);
		_uiSubCatName = (TextView)this.findViewById(R.id.subcategoryname);
		
		/* adapter for the list. */
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _subCatList);
		_uiSubCatList.setAdapter(aa);
		
		/* title. */
		_uiSubCatName.setText(_subCatName);
		
		/* event listener. */
		_uiSubCatList.setOnItemClickListener(this);
	}
	
	/** handler: onItemClick. */
	public void onItemClick(AdapterView<?> a, View v, int position, long id) 
	{
		// TODO Auto-generated method stub
		if(a.getId() == R.id.subcategorylist)
		{
			String catName = (String)a.getItemAtPosition(position);
			String qstring = anarxiv._urlTbl.getQueryString(catName);
			
			Intent intent = new Intent(this, PaperListWnd.class);
			intent.putExtra("category", qstring);
			intent.putExtra("categoryname", catName);
			startActivity(intent);
		}
	}
}
