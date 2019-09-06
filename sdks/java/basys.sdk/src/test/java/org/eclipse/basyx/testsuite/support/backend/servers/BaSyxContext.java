package org.eclipse.basyx.testsuite.support.backend.servers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.AssetAdministrationShell;



/**
 * BaSyx context that contains an Industrie 4.0 Servlet infrastructure 
 * 
 * @author kuhn
 *
 */
public class BaSyxContext extends HashMap<String, HttpServlet> {

	
	/**
	 * Version of serialized instance
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Requested server context path
	 */
	protected String contextPath;
	
	
	/**
	 * Requested server documents base path
	 */
	protected String docBasePath;
	
	/**
	 * hostname, e.g. ip or localhost
	 */
	protected String hostname;

	/**
	 * Requested Tomcat apache port
	 */
	protected int port;
	
	
	/**
	 * Servlet parameter
	 */
	protected Map<String, Map<String, String>> servletParameter = new HashMap<>();


	public Object AASHTTPServerResource;

	
	protected List<AssetAdministrationShell> aasList;


	
	/**
	 * Constructor with default port
	 */
	public BaSyxContext(String reqContextPath, String reqDocBasePath) {
		// Invoke constructor
		this(reqContextPath, reqDocBasePath, "localhost", 8080);
		
		this.aasList = new ArrayList<AssetAdministrationShell>();
	}

	
	/**
	 * Constructor
	 */
	public BaSyxContext(String reqContextPath, String reqDocBasePath, String hostn, int reqPort) {
		// Store context path and doc base path
		contextPath = reqContextPath;
		docBasePath = reqDocBasePath;
		hostname = hostn;
		port        = reqPort;
		
		this.aasList = new ArrayList<AssetAdministrationShell>();

	}
	
	
	
	/**
	 * Add a servlet mapping
	 */
	public BaSyxContext addServletMapping(String key, HttpServlet servlet) {
		// Add servlet mapping
		put(key, servlet);

		// Return 'this' reference to enable chaining of operations
		return this;
	}
	
	
	/**
	 * Store AAS provided in this context
	 * @param fs01
	 */
	public void addAAS(AssetAdministrationShell aas) {
		this.aasList.add(aas);
	}
	
	public List<AssetAdministrationShell> getAASList() {
		return this.aasList;
	}
	

	/**
	 * Add a servlet mapping with parameter
	 */
	public BaSyxContext addServletMapping(String key, HttpServlet servlet, Map<String, String> servletParameter) {
		// Add servlet mapping
		addServletMapping(key, servlet);
		
		// Add Servlet parameter
		addServletParameter(key, servletParameter);

		// Return 'this' reference to enable chaining of operations
		return this;
	}

	
	/**
	 * Add servlet parameter
	 */
	public BaSyxContext addServletParameter(String key, Map<String, String> parameter) {
		// Add servlet parameter
		servletParameter.put(key, parameter);
		
		// Return 'this' reference to enable chaining of operations
		return this;
	}
	
	
	
	/**
	 * Get servlet parameter
	 */
	public Map<String, String> getServletParameter(String key) {
		// Return servlet parameter iff parameter map contains requested key
		if (servletParameter.containsKey(key)) return servletParameter.get(key);
		
		// Return empty map
		return new HashMap<String, String>();
	}
	
	
	/**
	 * Return Tomcat server port
	 */
	public int getPort() {
		return port;
	}


	

}

