package org.eclipse.basyx.examples.snippets.aas;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.map.SubModel;

/**
 * This snippet showcases how to add a Submodel to an AAS,
 * that already exists on a server
 * 
 * @author conradi
 *
 */
public class AddSubmodelToAAS {

	/**
	 * Adds a Submodel to an AAS and registers it
	 * 
	 * @param submodel the Submodel to be added
	 * @param aasIdentifier the Identifier of the AAS the Submodel should be added to
	 * @param registryServerURL the URL of the registry server
	 */
	public static void addSubmodelToAAS(SubModel submodel, IIdentifier aasIdentifier, String registryServerURL) {

		// Create a proxy pointing to the registry server
		AASRegistryProxy registryProxy = new AASRegistryProxy(registryServerURL);
		
		// Create a ConnectedAASManager using the registryProxy as its registry
		ConnectedAssetAdministrationShellManager manager =
				new ConnectedAssetAdministrationShellManager(registryProxy);
		
		// Add the submodel to the AAS using the ConnectedAASManager
		// The manager pushes the submodel to the server and registers it
		// For this to work, the Identification of the Submodel has to be set
		manager.createSubModel(aasIdentifier, submodel);
		
	}
	
}
