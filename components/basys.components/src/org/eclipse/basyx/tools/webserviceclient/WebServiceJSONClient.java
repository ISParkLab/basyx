package org.eclipse.basyx.tools.webserviceclient;

import org.eclipse.basyx.aas.api.exception.ServerException;
import org.eclipse.basyx.aas.backend.http.tools.JSONTools;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Helper class that supports invocation of remote web services. The class implement serialization/deserialization from and to JSON types
 * 
 * @author kuhn
 *
 */
public class WebServiceJSONClient {

	
	/**
	 * Web service raw client instance
	 */
	protected WebServiceRawClient client = new WebServiceRawClient();

	
	
	/**
	 * Get result from webservice invocation
	 * 
	 * @throws ServerException
	 */
	protected Object getJSONResult(String serializedJSONValue) {
		// Try to deserialize response if any
		try {
			// Try to deserialize response
			Object result = JSONTools.Instance.deserialize(new JSONObject(serializedJSONValue));
			
			// Check if a server exception was serialized
			if (result instanceof ServerException) {
				// Throw server exception
				throw (ServerException) result;
			}
			
			// Return deserialized value
			return result;
			
		// Catch exceptions that did occur during deserialization, return null in this case
		} catch (JSONException e) {
			e.printStackTrace();
			// If there is no return value or deserialization failed
			return null;
		}		
	}
	

	
	
	/**
	 * Execute a web service, return deserialized object
	 */
	public Object get(String wsURL) {
		// Execute web service call, receive JSON serialized result
		String jsonResult = client.get(wsURL);
		
		// Return deserialized value
		return getJSONResult(jsonResult);
	}

	
	/**
	 * Execute a web service put operation, return JSON string
	 */
	public Object put(String wsURL, Object newValue) {
		// Serialize new value to JSON Object
		JSONObject jsonObject = JSONTools.Instance.serialize(newValue);

		// Execute web service call, receive JSON serialized result
		String jsonResult = client.put(wsURL, jsonObject.toString());

		// Return deserialized value
		return getJSONResult(jsonResult);
	}
	
	
	
	/**
	 * Execute a web service post operation, return JSON string
	 */
	public Object post(String wsURL, String... parameter) {
		// Serialize new value to JSON Object
		JSONObject jsonObject = JSONTools.Instance.serialize(parameter);

		// Perform request
		String jsonResult = client.post(wsURL, jsonObject.toString());
		
		System.out.println("Result:"+jsonResult);

		// Return deserialized value
		return getJSONResult(jsonResult);
	}

	
	
	/**
	 * Execute a web service patch operation, return JSON string
	 */
	public Object patch(String wsURL, String action, String... parameter) {
		// Serialize new value to JSON Object
		JSONObject jsonObject = JSONTools.Instance.serialize(parameter);

		// Perform request
		String jsonResult = client.patch(wsURL, action, jsonObject.toString());

		// Return deserialized value
		return getJSONResult(jsonResult);
	}

	
	
	/**
	 * Execute a web service delete operation, return JSON string
	 */
	public Object delete(String wsURL) {
		// Execute web service call, receive JSON serialized result
		String jsonResult = client.delete(wsURL);
		
		// Return deserialized value
		return getJSONResult(jsonResult);
	}
}
