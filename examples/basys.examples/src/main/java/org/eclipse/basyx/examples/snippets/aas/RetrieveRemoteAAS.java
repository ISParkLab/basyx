package org.eclipse.basyx.examples.snippets.aas;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;

/**
 * This snippet showcases how to retrieve an AAS from a server
 * 
 * @author conradi
 *
 */
public class RetrieveRemoteAAS {

	/**
	 * Get an AAS from a server
	 * 
	 * @param aasIdentifier the Identifier of the requested AAS
	 * @param registryServerURL the URL of the registry server
	 * @return The requested AAS as ConnectedAAS
	 */
	public static IAssetAdministrationShell retrieveRemoteAAS(IIdentifier aasIdentifier, String registryServerURL) {

		// Create a proxy pointing to the registry server
		AASRegistryProxy registryProxy = new AASRegistryProxy(registryServerURL);
		
		// Create a ConnectedAASManager using the registryProxy as its registry
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(registryProxy);
		
		// Get the requested AAS from the manager
		return manager.retrieveAAS(aasIdentifier);
	}
	
}
