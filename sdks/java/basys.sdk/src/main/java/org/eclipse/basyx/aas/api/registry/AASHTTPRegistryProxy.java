package org.eclipse.basyx.aas.api.registry;

import java.net.URLEncoder;
import java.util.Map;

import org.eclipse.basyx.aas.api.modelurn.ModelUrn;
import org.eclipse.basyx.aas.api.webserviceclient.WebServiceRawClient;
import org.eclipse.basyx.aas.backend.connector.MetaprotocolHandler;
import org.eclipse.basyx.aas.backend.http.tools.GSONTools;
import org.eclipse.basyx.aas.backend.http.tools.factory.DefaultTypeFactory;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.descriptor.AASDescriptor;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.identifier.IdentifierType;
import org.eclipse.basyx.vab.core.IVABDirectoryService;




/**
 * Local proxy class that hides HTTP calls to BaSys registry
 * 
 * @author kuhn
 *
 */
public class AASHTTPRegistryProxy implements IAASRegistryService, IVABDirectoryService {

	
	/**
	 * Store AAS registry URL
	 */
	protected String aasRegistryURL = null;

	
	/**
	 * Invoke BaSyx service calls via web services
	 */
	protected WebServiceRawClient client = null;
	
	
	/**
	 * JSON serializer
	 */
	protected GSONTools serializer = null;

	
	
	
	
	/**
	 * Constructor
	 */
	public AASHTTPRegistryProxy(String aasRegURL) {
		// Store URL
		aasRegistryURL = aasRegURL;
		
		// Create web service client
		client = new WebServiceRawClient();
		
		// Create serializer
		serializer = new GSONTools(new DefaultTypeFactory());
	}
	
	
	/**
	 * Register AAS descriptor in registry, delete old registration
	 */
	@Override
	public void register(ModelUrn aasID, AASDescriptor deviceAASDescriptor) {
		// Invoke delete operation of AAS registry
		try {client.delete(aasRegistryURL+"/api/v1/registry/"+URLEncoder.encode(aasID.getURN(), "UTF-8"));} catch (Exception e) {e.printStackTrace();}

		// Perform web service call to registry
		client.post(aasRegistryURL+"/api/v1/registry", serializer.serialize(deviceAASDescriptor));
	}


	
	/**
	 * Register AAS descriptor in registry
	 */
	@Override
	public void registerOnly(AASDescriptor deviceAASDescriptor) {
		// Perform web service call to registry
		client.post(aasRegistryURL+"/api/v1/registry", serializer.serialize(deviceAASDescriptor));
	}
	
	
	/**
	 * Delete AAS descriptor from registry
	 */
	@Override
	public void delete(ModelUrn aasID) {
		// Invoke delete operation of AAS registry
		try {client.delete(aasRegistryURL+"/api/v1/registry/"+URLEncoder.encode(aasID.getURN(), "UTF-8"));} catch (Exception e) {e.printStackTrace();}
	}
	
	
	/**
	 * Lookup device AAS
	 */
	@Override @SuppressWarnings("unchecked")
	public AASDescriptor lookupAAS(ModelUrn aasID) {
		// Lookup AAS from AAS directory, get AAS descriptor
		String jsonResponse = client.get(aasRegistryURL+"/api/v1/registry/"+aasID.getEncodedURN());
		
		// Deserialize AAS descriptor
		AASDescriptor aasDescriptor = null;
		try {
			aasDescriptor = new AASDescriptor((Map<String, Object>) (new MetaprotocolHandler().verify(jsonResponse)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Return AAS descriptor
		return aasDescriptor;
	}
	
	
	
	/**
	 * Add an AAS mapping to a directory
	 * 
	 * This function creates an AAS descriptor and registers it in the directory
	 */
	@Override
	public IAASRegistryService addAASMapping(String key, String value) {
		// Create AAS descriptor and set ID, ID type, and endpoint
		AASDescriptor aasDescriptor = new AASDescriptor(key, IdentifierType.URI, value);

		// Push AAS descriptor to server
		client.post(aasRegistryURL+"/api/v1/registry", serializer.serialize(aasDescriptor));
		
		// Return 'this' reference
		return this;
	}


	/**
	 * Delete an AAS mapping
	 */
	@Override
	public void removeMapping(String key) {
		// Invoke delete operation of AAS registry
		try {client.delete(aasRegistryURL+"/api/v1/registry/"+key);} catch (Exception e) {e.printStackTrace();}
	}


	/**
	 * Lookup one AAS mapping
	 */
	@Override
	public String lookup(String id) {
		// Lookup AAS from AAS directory, get AAS descriptor
		String jsonData = client.get(aasRegistryURL+"/api/v1/registry/"+id);

		// Return endpoint
		return jsonData;
	}


	/**
	 * Return all locally registered mappings
	 */
	@Override
	public Map<String, String> getMappings() {
		// Currently not implemented
		return null;
	}


	@Override
	public IVABDirectoryService addMapping(String key, String value) {
		// Currently not implemented
		return null;
	}

}

