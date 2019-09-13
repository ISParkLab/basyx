package org.eclipse.basyx.vab.backend.server.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.basyx.vab.backend.http.tools.GSONTools;
import org.eclipse.basyx.vab.backend.http.tools.factory.DefaultTypeFactory;
import org.eclipse.basyx.vab.backend.server.utils.IResult;
import org.eclipse.basyx.vab.backend.server.utils.Result;

/**
 * HTTP Servelet superclass to enable HTTP Patch
 * @author pschorn
 *
 */
public abstract class BasysHTTPServlet extends HttpServlet {

	
	/**
	 * Version of serialized instances
	 */
	private static final long serialVersionUID = 1L;
	
		
	/**
	 * Parameter map
	 */
	protected Map<String, String> servletParameter = new HashMap<>();

	/**
	 * GSON instance
	 */
	protected GSONTools serializer = new GSONTools(new DefaultTypeFactory());

	
	
	/**
	 * Dispatch service call 
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("PATCH")){
           doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

	
	/**
	 * Implement Patch request 
	 */
    protected abstract void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    
    
    /**
     * Add a servlet parameter
     */
    
    public BasysHTTPServlet withParameter(String parameter, String value) {
    	// Add parameter
    	servletParameter.put(parameter, value);
    	
    	// Return this instance
    	return this;
    }
    
    
	/**
	 * Get init parameter of servlet
	 */
	@Override
	public String getInitParameter(String name) {
		// Check if servletParameter map contains requested parameter
		if (servletParameter.containsKey(name)) return servletParameter.get(name);

		// Call base method
		return super.getInitParameter(name);
	}

	/**
	 * Send a response
	 */
	protected void sendResponse(Object value, PrintWriter outputStream) {

		// Wrap the entity in the meta-protocol
		IResult result = new Result(true, value, null);

		// Output result
		outputStream.write(serializer.serialize(result));
		outputStream.flush();
	}

}

