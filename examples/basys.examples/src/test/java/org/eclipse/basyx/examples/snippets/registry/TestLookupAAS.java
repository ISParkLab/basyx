package org.eclipse.basyx.examples.snippets.registry;

import static org.junit.Assert.assertEquals;

import org.eclipse.basyx.aas.metamodel.map.descriptor.AASDescriptor;
import org.eclipse.basyx.examples.snippets.AbstractSnippetTest;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.map.identifier.Identifier;
import org.junit.Test;

/**
 * Test for the LookupAAS snippet
 * 
 * @author conradi
 *
 */
public class TestLookupAAS extends AbstractSnippetTest {

	@Test
	public void testLookupAAS() {
		
		// Get the Identifier of the example AAS
		IIdentifier aasIdentifier = new Identifier(IdentifierType.CUSTOM, AAS_ID);
		
		// Lookup the AAS in the registry
		AASDescriptor descriptor = LookupAAS.lookupAAS(aasIdentifier, registryComponent.getRegistryPath());
		
		// Check if the returned Descriptor is as expected
		assertEquals(AAS_ENDPOINT, descriptor.getFirstEndpoint());
		
	}
	
}
