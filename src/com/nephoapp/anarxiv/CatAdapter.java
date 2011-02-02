package com.nephoapp.anarxiv;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CatAdapter extends ArrayAdapter<String> 
{
	public CatAdapter(Context _context, int _resource, String[] strings)
	{
		super(_context, _resource, strings);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View newview=super.getView(position, convertView, parent);
		if (!anarxiv.Catflag[position])
		{
			newview.setBackgroundResource(R.color.candidate_background1);
		}
		else
		{
			newview.setBackgroundResource(R.color.candidate_background2);
		}
	
		return newview;
		
		
	}
	

}
