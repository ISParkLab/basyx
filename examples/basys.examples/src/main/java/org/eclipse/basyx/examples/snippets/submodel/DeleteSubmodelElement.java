package org.eclipse.basyx.examples.snippets.submodel;

import org.eclipse.basyx.examples.snippets.aas.RetrieveSubmodelFromAAS;
import org.eclipse.basyx.submodel.metamodel.api.ISubModel;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;

/**
 * Snippet that showcases how to delete a SubmodelElement from a Submodel
 * 
 * @author conradi
 *
 */
public class DeleteSubmodelElement {

	/**
	 * Removes a SubmodelElement from a Submodel
	 * 
	 * @param elementId the Id of the Element to be deleted (can also be a path if the Element is in a Collection)
	 * @param smIdentifier the Identifier of the Submodel the Element belongs to
	 * @param aasIdentifier the Identifier of the AAS the Submodel belongs to
	 * @param registryServerURL the URL of the registry server (e.g. http://localhost:8080/registry)
	 */
	public static void deleteSubmodelElement(String elementId, IIdentifier smIdentifier, IIdentifier aasIdentifier, String registryServerURL) {
	
		// Retrieve the Submodel from the server as a ConnectedSubmodel
		ISubModel submodel = RetrieveSubmodelFromAAS.retrieveSubmodelFromAAS(smIdentifier, aasIdentifier, registryServerURL);
		
		// Delete the Element from the Submodel
		submodel.deleteSubmodelElement(elementId);
	}
}
