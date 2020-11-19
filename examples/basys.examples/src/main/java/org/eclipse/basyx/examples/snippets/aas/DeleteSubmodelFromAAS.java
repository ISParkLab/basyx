package org.eclipse.basyx.examples.snippets.aas;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;

/**
 * Snippet that showcases how to delete a Submodel from a server
 * 
 * @author conradi
 *
 */
public class DeleteSubmodelFromAAS {

	/**
	 * Removes a Submodel from an AAS
	 * 
	 * @param smIdentifier the Identifier of the Submodel to be deleted
	 * @param aasIdentifier the Identifier of the AAS the Submodel belongs to
	 * @param registryServerURL the URL of the registry server (e.g. http://localhost:8080/registry)
	 */
	public static void deleteSubmodelFromAAS(IIdentifier smIdentifier, IIdentifier aasIdentifier, String registryServerURL) {
	
		// Create a proxy pointing to the registry server
		AASRegistryProxy registryProxy = new AASRegistryProxy(registryServerURL);
		
		// Create a ConnectedAASManager using the registryProxy as its registry
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(registryProxy);
		
		// Delete the Submodel
		// Automatically deregisters it
		manager.deleteSubModel(aasIdentifier, smIdentifier);
	}
}