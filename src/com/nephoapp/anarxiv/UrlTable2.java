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

package com.nephoapp.anarxiv;

import java.util.Collection;
import java.util.TreeMap;

import com.nephoapp.anarxiv.UrlTable.CategoryItem;

/**
 *
 *
 */
public class UrlTable2 
{
	/**
	 * 
	 */
	public static class CategoryItem
	{
		/**
		 * 
		 */
		public CategoryItem()
		{
			this(null, null, null);
		}
		
		/**
		 * 
		 * @param name
		 */
		public CategoryItem(String name)
		{
			this(name, null, null);
		}
		
		/**
		 * 
		 * @param name
		 * @param url
		 */
		public CategoryItem(String name, String url)
		{
			this(name, url, null);
		}
		
		/**
		 * 
		 * @param name
		 * @param url
		 */
		public CategoryItem(String name, String url, TreeMap<String, CategoryItem> subcategory)
		{
			_name = name;
			_url = url;
			_subcategory = subcategory;
		}
		
		/** the user-friendly name of this category. */
		public String _name;
		
		/** the corresponding url; null if this item is a sub-category. */
		public String _url;
		
		/** a tree map pointing to child category. */
		public TreeMap<String, CategoryItem> _subcategory;
	}
	
	/**
	 * the root category.
	 */
	private TreeMap<String, CategoryItem> _rootCategory;
	
	/** 
	 * sub category: astrophysics.
	 */
	private TreeMap<String, CategoryItem> _UrlMap_Astrophysics = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: condensed matter. 
	 */
	private TreeMap<String, CategoryItem> _UrlMap_CondensedMatter = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: high energy physics. 
	 */
	private TreeMap<String, CategoryItem> _UrlMap_HEP = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: nuclear.
	 */
	private TreeMap<String, CategoryItem> _UrlMap_Nuclear = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: relativity.
	 */
	private TreeMap<String, CategoryItem> _UrlMap_Relativity = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: math physics.
	 */
	private TreeMap<String, CategoryItem> _UrlMap_MathPhysics = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: quantum physics.
	 */
	private TreeMap<String, CategoryItem> _UrlMap_QuantumPhysics = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: physics.
	 */
	/* TODO: missing: General Relativity and Quantum Cosmology. */
	private TreeMap<String, CategoryItem> _UrlMap_Physics = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: mathematics.
	 */
	private TreeMap<String, CategoryItem> _UrlMap_Math = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: nonlinear science. 
	 */
	private TreeMap<String, CategoryItem> _UrlMap_NonlinearSci = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: computer science.
	 */
	private TreeMap<String, CategoryItem> _UrlMap_CS = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: quantitative biology. 
	 */
	private TreeMap<String, CategoryItem> _UrlMap_QuantBio = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: quantitative finance. 
	 */
	private TreeMap<String, CategoryItem> _UrlMap_QuantFinance = new TreeMap<String, CategoryItem>();
	
	/** 
	 * sub category: statistics. 
	 */
	private TreeMap<String, CategoryItem> _UrlMap_Statistics = new TreeMap<String, CategoryItem>();
	
	
	/**
	 * 
	 */
	public UrlTable2()
	{
		
	}
	
	private void buildCategory()
	{
		
	}
	
	private void buildUrlMap()
	{
		/* astrophysics. */
		this._UrlMap_Astrophysics.put("Cosmology and Extragalactic", 	new CategoryItem("Cosmology and Extragalactic", "astro-ph.CO"));
		this._UrlMap_Astrophysics.put("Earth and Planetary", 			new CategoryItem("Earth and Planetary", "astro-ph.EP"));
		this._UrlMap_Astrophysics.put("Galaxy", 						new CategoryItem("Galaxy", "astro-ph.GA"));
		this._UrlMap_Astrophysics.put("High Energy Phenomena", 			new CategoryItem("High Energy Phenomena", "astro-ph.HE"));
		this._UrlMap_Astrophysics.put("Instrumentation and Methods", 	new CategoryItem("Instrumentation and Methods", "astro-ph.IM"));
		this._UrlMap_Astrophysics.put("Solar and Stellar", 				new CategoryItem("Solar and Stellar", "astro-ph.SR"));
	}
}
