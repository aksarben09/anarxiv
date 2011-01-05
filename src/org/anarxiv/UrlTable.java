package org.anarxiv;

import java.util.HashMap;

/**
 * This class stores url and corresponding descriptions that will be displayed 
 * on the gui.
 * 
 * @author lihe
 *
 */
public class UrlTable 
{
	/** main category. */
	public final static String[] Category = {"Astrophysics",
											 "Condensed Matter",
											 "Physics",
											 "Mathematics",
											 "Nonlinear Sciences",
											 "Computer Science",
											 "Quantitative Biology",
											 "Quantitative Finance",
											 "Statistics"};
	
	/** sub category: physics. */
	/* TODO: missing: General Relativity and Quantum Cosmology, HEP, Nuclear. */
	public final static String[] Subcategory_Physics 
		= {"Accelerator Physics",
		   "Atmospheric and Oceanic Physics",
		   "Atomic Physics",
		   "Atomic and Molecular Clusters",
		   "Biological Physics",
		   "Chemical Physics",
		   "Classical Physics",
		   "Computational Physics",
		   "Data Analysis, Statistics and Probability",
		   "Fluid Dynamics",
		   "General Physics",
		   "Geophysics",
		   "History of Physics",
		   "Instrumentation and Detectors",
		   "Medical Physics",
		   "Optics",
		   "Physics Education",
		   "Physics and Society",
		   "Plasma Physics",
		   "Popular Physics",
		   "Space Physics"};
	
	/** sub category: mathematics. */
	public final static String[] Subcategory_Mathematics 
		= {"Algebraic Geometry",
		   "Algebraic Topology", 
		   "Analysis of PDEs", 
		   "Category Theory",
		   "Classical Analysis and ODEs",
		   "Combinatorics",
		   "Commutative Algebra", 
		   "Complex Variables", 
		   "Differential Geometry", 
		   "Dynamical Systems", 
		   "Functional Analysis", 
		   "General Mathematics", 
		   "General Topology",
		   "Geometric Topology", 
		   "Group Theory", 
		   "History and Overview", 
		   "Information Theory", 
		   "K-Theory and Homology", 
		   "Logic", 
		   "Mathematical Physics",
		   "Metric Geometry", 
		   "Number Theory",
		   "Numerical Analysis", 
		   "Operator Algebras", 
		   "Optimization and Control", 
		   "Probability", 
		   "Quantum Algebra", 
		   "Representation Theory", 
		   "Rings and Algebras", 
		   "Spectral Theory", 
		   "Statistics Theory", 
		   "Symplectic Geometry"};
	
	/** sub category: nonlinear science. */
	public final static String[] Subcategory_NonlinearSci 
		= {"Adaptation and Self-Organizing Systems",
		   "Cellular Automata and Lattice Gases",
		   "Chaotic Dynamics",
		   "Exactly Solvable and Integrable Systems",
		   "Pattern Formation and Solitons"};
	
	/** sub category: computer science. */
	public final static String[] Subcategory_CS 
		= {"Artificial Intelligence", 
		   "Computation and Language", 
		   "Computational Complexity",
		   "Computational Engineering, Finance, and Science", 
		   "Computational Geometry",
		   "Computer Science and Game Theory", 
		   "Computer Vision and Pattern Recognition", 
		   "Computers and Society",
		   "Cryptography and Security",
		   "Data Structures and Algorithms",
		   "Databases",
		   "Digital Libraries",
		   "Discrete Mathematics", 
		   "Distributed, Parallel, and Cluster Computing",
		   "Formal Languages and Automata Theory", 
		   "General Literature", 
		   "Graphics", 
		   "Hardware Architecture", 
		   "Human-Computer Interaction", 
		   "Information Retrieval",
		   "Information Theory", 
		   "Learning", 
		   "Logic in Computer Science",
		   "Mathematical Software",
		   "Multiagent Systems",
		   "Multimedia",
		   "Networking and Internet Architecture",
		   "Neural and Evolutionary Computing",
		   "Numerical Analysis",
		   "Operating Systems",
		   "Other Computer Science",
		   "Performance",
		   "Programming Languages",
		   "Robotics",
		   "Social and Information Networks",
		   "Software Engineering",
		   "Sound",
		   "Symbolic Computation",
		   "Systems and Control"};
	
	/** sub category: quantitative biology. */
	public final static String[] Subcategory_QuantBio 
		= {"Biomolecules",
		   "Cell Behavior",
		   "Genomics",
		   "Molecular Networks",
		   "Neurons and Cognition",
		   "Other Quantitative Biology",
		   "Populations and Evolution",
		   "Quantitative Methods",
		   "Subcellular Processes",
		   "Tissues and Organs"};
	
	/** sub category: quantitative finance. */
	public final static String[] Subcategory_QuantFinance 
		= {"Computational Finance",
		   "General Finance",
		   "Portfolio Management",
		   "Pricing of Securities",
		   "Risk Management",
		   "Statistical Finance",
		   "Trading and Market Microstructure"};
	
	/** sub category: statistics. */
	public final static String[] Subcategory_Statistics 
		= {"Applications",
		   "Computation",
		   "Machine Learning",
		   "Methodology",
		   "Other Statistics",
		   "Statistics Theory"};
	
	/** category-to-subcategory map. */
	private HashMap<String, String[]> _SubcategoryMap = new HashMap<String, String[]>();
	
	/** subcategory-to-url map. */
	private HashMap<String, String> _UrlMap = new HashMap<String, String>();
	
	/** 
	 * constructor.
	 * maps are built here.
	 */
	public UrlTable()
	{
		this.buildSubcategoryMap();
		this.buildUrlMap();
	}
	
	/**
	 * build _SubcategoryMap.
	 */
	private void buildSubcategoryMap()
	{
		this._SubcategoryMap.put(UrlTable.Category[2], UrlTable.Subcategory_Physics);
		this._SubcategoryMap.put(UrlTable.Category[3], UrlTable.Subcategory_Mathematics);
		this._SubcategoryMap.put(UrlTable.Category[4], UrlTable.Subcategory_NonlinearSci);
		this._SubcategoryMap.put(UrlTable.Category[5], UrlTable.Subcategory_CS);
		this._SubcategoryMap.put(UrlTable.Category[6], UrlTable.Subcategory_QuantBio);
		this._SubcategoryMap.put(UrlTable.Category[7], UrlTable.Subcategory_QuantFinance);
		this._SubcategoryMap.put(UrlTable.Category[8], UrlTable.Subcategory_Statistics);
	}
	
	/**
	 * build _UrlMap.
	 */
	private void buildUrlMap()
	{
		
	}
}
