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
	private TreeMap<String, CategoryItem> _rootCategory = new TreeMap<String, CategoryItem>();
	
	/**
	 * 
	 */
	private TreeMap<String, CategoryItem> _UrlMap_GreaterPhysics = new TreeMap<String, CategoryItem>();
	
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
		buildUrlMap();
		buildCategory();
	}
	
	private void buildCategory()
	{
		_rootCategory.put("Physics",				new CategoryItem("Physics", null, _UrlMap_Physics));
		_rootCategory.put("Mathematics",			new CategoryItem("Mathematics", null, _UrlMap_Math));
		_rootCategory.put("Nonlinear Science",		new CategoryItem("Nonlinear Science", null, _UrlMap_NonlinearSci));
		_rootCategory.put("Computer Science",		new CategoryItem("Computer Science", null, _UrlMap_CS));
		_rootCategory.put("Quantitative Biology",	new CategoryItem("Quantitative Biology", null, _UrlMap_QuantBio));
		_rootCategory.put("Quantitative Finance",	new CategoryItem("Quantitative Finance", null, _UrlMap_QuantFinance));
		_rootCategory.put("Statistics",				new CategoryItem("Statistics", null, _UrlMap_Statistics));
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
		
		/* condensed matter. */
		this._UrlMap_CondensedMatter.put("Disordered Systems and Neural Networks",	new CategoryItem("Disordered Systems and Neural Networks", "cond-mat.dis-nn"));
		this._UrlMap_CondensedMatter.put("Materials Science", 						new CategoryItem("Materials Science", "cond-mat.mtrl-sci"));
		this._UrlMap_CondensedMatter.put("Mesoscale and Nanoscale Physics", 		new CategoryItem("Mesoscale and Nanoscale Physics", "cond-mat.mes-hall"));
		this._UrlMap_CondensedMatter.put("Other Condensed Matter", 					new CategoryItem("Other Condensed Matter", "cond-mat.other"));
		this._UrlMap_CondensedMatter.put("Quantum Gases", 							new CategoryItem("Quantum Gases", "cond-mat.quant-gas"));
		this._UrlMap_CondensedMatter.put("Soft Condensed Matter", 					new CategoryItem("Soft Condensed Matter", "cond-mat.soft"));
		this._UrlMap_CondensedMatter.put("Statistical Mechanics", 					new CategoryItem("Statistical Mechanics", "cond-mat.stat-mech"));
		this._UrlMap_CondensedMatter.put("Strongly Correlated Electrons", 			new CategoryItem("Strongly Correlated Electrons", "cond-mat.str-el"));
		this._UrlMap_CondensedMatter.put("Superconductivity", 						new CategoryItem("Superconductivity", "cond-mat.supr-con"));
		
		/* high energy physics. */
		this._UrlMap_HEP.put("Experiment",		new CategoryItem("Experiment", "hep-ex"));
		this._UrlMap_HEP.put("Lattice",			new CategoryItem("Lattice", "hep-lat"));
		this._UrlMap_HEP.put("Phenomenology",	new CategoryItem("Phenomenology", "hep-ph"));
		this._UrlMap_HEP.put("Theory",			new CategoryItem("Theory", "hep-th"));
		
		/* nuclear. */
		this._UrlMap_Nuclear.put("Experiment", 	new CategoryItem("nucl-ex"));
		this._UrlMap_Nuclear.put("Theory", 		new CategoryItem("nucl-th"));
		
		/* physics. */
		this._UrlMap_Physics.put("Accelerator Physics", 						new CategoryItem("Accelerator Physics", "physics.acc-ph"));
		this._UrlMap_Physics.put("Atmospheric and Oceanic Physics", 			new CategoryItem("Atmospheric and Oceanic Physics", "physics.ao-ph"));
		this._UrlMap_Physics.put("Atomic Physics", 								new CategoryItem("Atomic Physics", "physics.atom-ph"));
		this._UrlMap_Physics.put("Atomic and Molecular Clusters", 				new CategoryItem("Atomic and Molecular Clusters", "physics.atm-clus"));
		this._UrlMap_Physics.put("Biological Physics", 							new CategoryItem("Biological Physics", "physics.bio-ph"));
		this._UrlMap_Physics.put("Chemical Physics", 							new CategoryItem("Chemical Physics", "physics.chem-ph"));
		this._UrlMap_Physics.put("Classical Physics", 							new CategoryItem("Classical Physics", "physics.class-ph"));
		this._UrlMap_Physics.put("Computational Physics", 						new CategoryItem("Computational Physics", "physics.comp-ph"));
		this._UrlMap_Physics.put("Data Analysis, Statistics and Probability", 	new CategoryItem("Data Analysis, Statistics and Probability", "physics.data-an"));
		this._UrlMap_Physics.put("Fluid Dynamics", 								new CategoryItem("physics.flu-dyn"));
		this._UrlMap_Physics.put("General Physics", 							new CategoryItem("physics.gen-ph"));
		this._UrlMap_Physics.put("Geophysics", 									new CategoryItem("physics.geo-ph"));
		this._UrlMap_Physics.put("History of Physics", 							new CategoryItem("physics.hist-ph"));
		this._UrlMap_Physics.put("Instrumentation and Detectors", 				new CategoryItem("physics.ins-det"));
		this._UrlMap_Physics.put("Medical Physics", 							new CategoryItem("physics.med-ph"));
		this._UrlMap_Physics.put("Optics", 										new CategoryItem("physics.optics"));
		this._UrlMap_Physics.put("Physics Education", 							new CategoryItem("physics.ed-ph"));
		this._UrlMap_Physics.put("Physics and Society", 						new CategoryItem("physics.soc-ph"));
		this._UrlMap_Physics.put("Plasma Physics", 								new CategoryItem("physics.plasm-ph"));
		this._UrlMap_Physics.put("Popular Physics", 							new CategoryItem("physics.pop-ph"));
		this._UrlMap_Physics.put("Space Physics", 								new CategoryItem("physics.space-ph"));
		
		/* mathematics. */
		this._UrlMap_Math.put("Algebraic Geometry", 			new CategoryItem("math.AG"));
		this._UrlMap_Math.put("Algebraic Topology", 			new CategoryItem("math.AT"));
		this._UrlMap_Math.put("Analysis of PDEs", 				new CategoryItem("math.AP"));
		this._UrlMap_Math.put("Category Theory", 				new CategoryItem("math.CT"));
		this._UrlMap_Math.put("Classical Analysis and ODEs", 	new CategoryItem("math.CA"));
		this._UrlMap_Math.put("Combinatorics", 					new CategoryItem("math.CO"));
		this._UrlMap_Math.put("Commutative Algebra", 			new CategoryItem("math.AC"));
		this._UrlMap_Math.put("Complex Variables", 				new CategoryItem("math.CV"));
		this._UrlMap_Math.put("Differential Geometry", 			new CategoryItem("math.DG"));
		this._UrlMap_Math.put("Dynamical Systems", 				new CategoryItem("math.DS"));
		this._UrlMap_Math.put("Functional Analysis", 			new CategoryItem("math.FA"));
		this._UrlMap_Math.put("General Mathematics", 			new CategoryItem("math.GM"));
		this._UrlMap_Math.put("General Topology", 				new CategoryItem("math.GN"));
		this._UrlMap_Math.put("Geometric Topology", 			new CategoryItem("math.GT"));
		this._UrlMap_Math.put("Group Theory", 					new CategoryItem("math.GR"));
		this._UrlMap_Math.put("History and Overview", 			new CategoryItem("math.HO"));
		this._UrlMap_Math.put("Information Theory", 			new CategoryItem("math.IT"));
		this._UrlMap_Math.put("K-Theory and Homology", 			new CategoryItem("math.KT"));
		this._UrlMap_Math.put("Logic", 							new CategoryItem("math.LO"));
		this._UrlMap_Math.put("Mathematical Physics", 			new CategoryItem("math.MP"));
		this._UrlMap_Math.put("Metric Geometry", 				new CategoryItem("math.MG"));
		this._UrlMap_Math.put("Number Theory", 					new CategoryItem("math.NT"));
		this._UrlMap_Math.put("Numerical Analysis", 			new CategoryItem("math.NA"));
		this._UrlMap_Math.put("Operator Algebras", 				new CategoryItem("math.OA"));
		this._UrlMap_Math.put("Optimization and Control", 		new CategoryItem("math.OC"));
		this._UrlMap_Math.put("Probability", 					new CategoryItem("math.PR"));
		this._UrlMap_Math.put("Quantum Algebra", 				new CategoryItem("math.QA"));
		this._UrlMap_Math.put("Representation Theory", 			new CategoryItem("math.RT"));
		this._UrlMap_Math.put("Rings and Algebras", 			new CategoryItem("math.RA"));
		this._UrlMap_Math.put("Spectral Theory", 				new CategoryItem("math.SP"));
		this._UrlMap_Math.put("Statistics Theory", 				new CategoryItem("math.ST"));
		this._UrlMap_Math.put("Symplectic Geometry", 			new CategoryItem("math.SG"));
		
		/* nonlinear science. */
		this._UrlMap_NonlinearSci.put("Adaptation and Self-Organizing Systems", 	new CategoryItem("nlin.AO"));
		this._UrlMap_NonlinearSci.put("Cellular Automata and Lattice Gases", 		new CategoryItem("nlin.CG"));
		this._UrlMap_NonlinearSci.put("Chaotic Dynamics", 							new CategoryItem("nlin.CD"));
		this._UrlMap_NonlinearSci.put("Exactly Solvable and Integrable Systems", 	new CategoryItem("nlin.SI"));
		this._UrlMap_NonlinearSci.put("Pattern Formation and Solitons", 			new CategoryItem("nlin.PS"));
		
		/* computer science. */	   
		this._UrlMap_CS.put("Artificial Intelligence", 							new CategoryItem("cs.AI"));
		this._UrlMap_CS.put("Computation and Language", 						new CategoryItem("cs.CL"));
		this._UrlMap_CS.put("Computational Complexity", 						new CategoryItem("cs.CC"));
		this._UrlMap_CS.put("Computational Engineering, Finance, and Science", 	new CategoryItem("cs.CE"));
		this._UrlMap_CS.put("Computational Geometry", 							new CategoryItem("cs.CG"));
		this._UrlMap_CS.put("Computer Science and Game Theory", 				new CategoryItem("cs.GT"));
		this._UrlMap_CS.put("Computer Vision and Pattern Recognition", 			new CategoryItem("cs.CV"));
		this._UrlMap_CS.put("Computers and Society", 							new CategoryItem("cs.CY"));
		this._UrlMap_CS.put("Cryptography and Security", 						new CategoryItem("cs.CR"));
		this._UrlMap_CS.put("Data Structures and Algorithms", 					new CategoryItem("cs.DS"));
		this._UrlMap_CS.put("Databases", 										new CategoryItem("cs.DB"));
		this._UrlMap_CS.put("Digital Libraries", 								new CategoryItem("cs.DL"));
		this._UrlMap_CS.put("Discrete Mathematics", 							new CategoryItem("cs.DM"));
		this._UrlMap_CS.put("Distributed, Parallel, and Cluster Computing", 	new CategoryItem("cs.DC"));
		this._UrlMap_CS.put("Formal Languages and Automata Theory", 			new CategoryItem("cs.FL"));
		this._UrlMap_CS.put("General Literature", 								new CategoryItem("cs.GL"));
		this._UrlMap_CS.put("Graphics", 										new CategoryItem("cs.GR"));
		this._UrlMap_CS.put("Hardware Architecture", 							new CategoryItem("cs.AR"));
		this._UrlMap_CS.put("Human-Computer Interaction", 						new CategoryItem("cs.HC"));
		this._UrlMap_CS.put("Information Retrieval", 							new CategoryItem("cs.IR"));
		this._UrlMap_CS.put("Information Theory", 								new CategoryItem("cs.IT"));
		this._UrlMap_CS.put("Learning", 										new CategoryItem("cs.LG"));
		this._UrlMap_CS.put("Logic in Computer Science", 						new CategoryItem("cs.LO"));
		this._UrlMap_CS.put("Mathematical Software", 							new CategoryItem("cs.MS"));
		this._UrlMap_CS.put("Multiagent Systems", 								new CategoryItem("cs.MA"));
		this._UrlMap_CS.put("Multimedia", 										new CategoryItem("cs.MM"));
		this._UrlMap_CS.put("Networking and Internet Architecture", 			new CategoryItem("cs.NI"));
		this._UrlMap_CS.put("Neural and Evolutionary Computing", 				new CategoryItem("cs.NE"));
		this._UrlMap_CS.put("Numerical Analysis", 								new CategoryItem("cs.NA"));
		this._UrlMap_CS.put("Operating Systems", 								new CategoryItem("cs.OS"));
		this._UrlMap_CS.put("Other Computer Science", 							new CategoryItem("cs.OH"));
		this._UrlMap_CS.put("Performance", 										new CategoryItem("cs.PF"));
		this._UrlMap_CS.put("Programming Languages", 							new CategoryItem("cs.PL"));
		this._UrlMap_CS.put("Robotics", 										new CategoryItem("cs.RO"));
		this._UrlMap_CS.put("Social and Information Networks", 					new CategoryItem("cs.SI"));
		this._UrlMap_CS.put("Software Engineering", 							new CategoryItem("cs.SE"));
		this._UrlMap_CS.put("Sound", 											new CategoryItem("cs.SD"));
		this._UrlMap_CS.put("Symbolic Computation", 							new CategoryItem("cs.SC"));
		this._UrlMap_CS.put("Systems and Control", 								new CategoryItem("cs.SY"));
		
		/* quantitative biology. */
		this._UrlMap_QuantBio.put("Biomolecules",				new CategoryItem("q-bio.BM"));
		this._UrlMap_QuantBio.put("Cell Behavior",				new CategoryItem("q-bio.CB"));
		this._UrlMap_QuantBio.put("Genomics",					new CategoryItem("q-bio.GN"));
		this._UrlMap_QuantBio.put("Molecular Networks",			new CategoryItem("q-bio.MN"));
		this._UrlMap_QuantBio.put("Neurons and Cognition",		new CategoryItem("q-bio.NC"));
		this._UrlMap_QuantBio.put("Other Quantitative Biology",	new CategoryItem("q-bio.OT"));
		this._UrlMap_QuantBio.put("Populations and Evolution",	new CategoryItem("q-bio.PE"));
		this._UrlMap_QuantBio.put("Quantitative Methods",		new CategoryItem("q-bio.QM"));
		this._UrlMap_QuantBio.put("Subcellular Processes",		new CategoryItem("q-bio.SC"));
		this._UrlMap_QuantBio.put("Tissues and Organs",			new CategoryItem("q-bio.TO"));
		
		/* quantitative finance. */
		this._UrlMap_QuantFinance.put("Computational Finance", 				new CategoryItem("q-fin.CP"));
		this._UrlMap_QuantFinance.put("General Finance", 					new CategoryItem("q-fin.GN"));
		this._UrlMap_QuantFinance.put("Portfolio Management", 				new CategoryItem("q-fin.PM"));
		this._UrlMap_QuantFinance.put("Pricing of Securities", 				new CategoryItem("q-fin.PR"));
		this._UrlMap_QuantFinance.put("Risk Management", 					new CategoryItem("q-fin.RM"));
		this._UrlMap_QuantFinance.put("Statistical Finance", 				new CategoryItem("q-fin.ST"));
		this._UrlMap_QuantFinance.put("Trading and Market Microstructure", 	new CategoryItem("q-fin.TR"));
		
		/* statistics. */		   
		this._UrlMap_Statistics.put("Applications", 		new CategoryItem("stat.AP"));
		this._UrlMap_Statistics.put("Computation", 			new CategoryItem("stat.CO"));
		this._UrlMap_Statistics.put("Machine Learning", 	new CategoryItem("stat.ML"));
		this._UrlMap_Statistics.put("Methodology", 			new CategoryItem("stat.ME"));
		this._UrlMap_Statistics.put("Other Statistics", 	new CategoryItem("stat.OT"));
		this._UrlMap_Statistics.put("Statistics Theory", 	new CategoryItem("stat.TH"));
	}
}
