package org.eclipse.basyx.examples.snippets.aas;

import static org.junit.Assert.assertEquals;

import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.examples.snippets.AbstractSnippetTest;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.map.identifier.Identifier;
import org.junit.Test;

/**
 * Test for the RetrieveRemoteAAS snippet
 * 
 * @author conradi
 *
 */
public class TestRetrieveRemoteAAS extends AbstractSnippetTest {
	
	@Test
	public void testRetrieveRemoteAAS() {
		
		// Get the Identifier of the example AAS
		IIdentifier aasIdentifier = new Identifier(IdentifierType.CUSTOM, AAS_ID);
		
		// Retrieve the AAS from the server
		IAssetAdministrationShell remoteAAS =
				RetrieveRemoteAAS.retrieveRemoteAAS(aasIdentifier, registryComponent.getRegistryPath());
		
		// Check if the retrieved AAS can be used correctly
		assertEquals(AAS_ID_SHORT, remoteAAS.getIdShort());
	}
}
