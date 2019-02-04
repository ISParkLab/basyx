package org.eclipse.basyx.components.servlets;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;

import org.eclipse.basyx.aas.backend.provider.VABMultiSubmodelProvider;
import org.eclipse.basyx.components.cfgprovider.RawCFGSubModelProvider;
import org.eclipse.basyx.vab.backend.server.http.VABHTTPInterface;
import org.eclipse.basyx.vab.provider.hashmap.VABHashmapProvider;




/**
 * Servlet interface for configuration file sub model provider
 * 
 * @author kuhn
 *
 */
public class RawCFGSubModelProviderServlet extends VABHTTPInterface<VABMultiSubmodelProvider<VABHashmapProvider>> {

	
	/**
	 * Version information to identify the version of serialized instances
	 */
	private static final long serialVersionUID = 1L;


	
	/**
	 * Store ID of the sub model provided by this provider
	 */
	protected String submodelID = null;
	

	/**
	 * Configuration properties
	 */
	protected Properties properties = null;
	
	
	
	/**
	 * Constructor
	 */
	public RawCFGSubModelProviderServlet() {
		// Invoke base constructor
		super(new VABMultiSubmodelProvider<VABHashmapProvider>());
	}
	
	/**
	 * Adds init parameter to servlet
	 */
	@Override
	public String getInitParameter(String name) {

		if (name.equals("config")) return "/WEB-INF/config/rawcfgprovider/samplecfg.properties";
		
		return null;
	}
	
	
	
	/**
	 * Load properties from file
	 */
	protected void loadProperties(String cfgFilePath) {
		// Read property file
		try {
			// Open property file
			InputStream input = getServletContext().getResourceAsStream(cfgFilePath); 

			// Instantiate property structure
			properties = new Properties();
			properties.load(input);
			
			// Extract AAS properties
			this.submodelID   = properties.getProperty("basyx.submodelID");
		} catch (IOException e) {
			// Output exception
			e.printStackTrace();
		}		
	}



	/**
	 * Initialize servlet
	 * 
	 * @throws ServletException 
	 */
	public void init() throws ServletException {
		// Call base implementation
		super.init();
		
		// Read configuration values
		String configFilePath = (String) getInitParameter("config");
		// - Read property file
		loadProperties(configFilePath);
		
		System.out.println("1:"+submodelID);
		
		// Create sub model provider
		RawCFGSubModelProvider submodelProvider = new RawCFGSubModelProvider(properties);
		// - Add sub model provider
		this.getModelProvider().addSubmodel(submodelID, submodelProvider);
		
		System.out.println("CFG file loaded");
	}
}

