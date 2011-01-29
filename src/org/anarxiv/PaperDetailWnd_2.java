/*
 * Copyright (C) 2011 Nephoapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.anarxiv;

import java.io.File;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;

public class PaperDetailWnd_2 extends PaperDetailWnd 
{
	/**
	 * paper id.
	 */
	private String _paperId = null;
	
	/**
	 * 
	 */
	private Bundle _savedInstanceState = null;
	
	/**
	 * loading thread.
	 */
	private class PaperDetailLoadingThread extends Thread
	{
		/**
		 * 
		 */
		@Override
		public void run()
		{
			try
			{
				final HashMap<String, Object> paperMap = ArxivLoader.loadPaperById(_paperId);
				
				PaperDetailWnd_2.this.runOnUiThread(new Runnable()
							{
								public void run()
								{
									Intent intent = PaperDetailWnd_2.this.getIntent();
									intent.putExtra("paperdetail", paperMap);
									PaperDetailWnd_2.this.onCreate(_savedInstanceState);
								}
							});
			}
			catch (ArxivLoader.LoaderException e)
			{
				/* just for the following runnable. */
				final ArxivLoader.LoaderException err = e;
				
				PaperDetailWnd_2.this.runOnUiThread(new Runnable()
								{
									public void run()
									{
										UiUtils.showToast(PaperDetailWnd_2.this, err.getMessage());
									}
								});
			}
		}
	}
	
	/**
	 * onCreate.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		/* get intent. */
		Intent intent = getIntent();
		_paperId = new File(intent.getStringExtra("id")).getName();
		
		/* load the detail. */
		try
		{
			HashMap<String, Object> paperMap = ArxivLoader.loadPaperById(_paperId);
			intent.putExtra("paperdetail", paperMap);
		}
		catch (ArxivLoader.LoaderException e)
		{
			UiUtils.showToast(PaperDetailWnd_2.this, e.getMessage());
		}
		
		super.onCreate(savedInstanceState);
	}
}
