package org.eclipse.basyx.testsuite.regression.aas.metamodel.connected;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.basyx.aas.factory.java.MetaModelElementFactory;
import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.AASDescriptor;
import org.eclipse.basyx.aas.metamodel.map.descriptor.ModelUrn;
import org.eclipse.basyx.aas.metamodel.map.descriptor.SubmodelDescriptor;
import org.eclipse.basyx.aas.registration.api.IAASRegistryService;
import org.eclipse.basyx.aas.registration.preconfigured.PreconfiguredRegistry;
import org.eclipse.basyx.aas.restapi.AASModelProvider;
import org.eclipse.basyx.aas.restapi.VABMultiSubmodelProvider;
import org.eclipse.basyx.submodel.metamodel.api.ISubModel;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.property.ISingleProperty;
import org.eclipse.basyx.submodel.metamodel.map.SubModel;
import org.eclipse.basyx.submodel.metamodel.map.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.property.SingleProperty;
import org.eclipse.basyx.submodel.restapi.SubModelProvider;
import org.eclipse.basyx.testsuite.regression.vab.protocol.TypeDestroyer;
import org.eclipse.basyx.testsuite.support.ConnectorProviderStub;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests if a ConnectedAssetAdministrationShell can be created and used
 * correctly
 * 
 * @author schnicke
 *
 */
public class TestConnectedAssetAdministrationShell {

	// String constants used in this test case
	private static final String smId = "smId";
	private static final String aasId = "aasId";
	private static final String propId = "propId";
	private static final int propVal = 11;

	IAssetAdministrationShell connectedAAS;

	@Before
	public void build() throws Exception {
		MetaModelElementFactory factory = new MetaModelElementFactory();

		// Create a SubModel containing no operations and one property
		SingleProperty p = new SingleProperty(propVal);
		p.setId(propId);

		SubModel sm = factory.create(new SubModel(), Collections.singletonList(p), new ArrayList<>());
		sm.setId(smId);

		// Create Set containing reference to the created SubModel
		Set<String> refs = new HashSet<>();
		refs.add(smId);

		// Create an AAS containing a reference to the created SubModel
		AssetAdministrationShell aas = factory.create(new AssetAdministrationShell(), refs);
		aas.setId(aasId);
	
		VABMultiSubmodelProvider provider = new VABMultiSubmodelProvider();
		provider.addSubmodel(smId, new SubModelProvider(TypeDestroyer.destroyType(sm)));
		provider.setAssetAdministrationShell(new AASModelProvider(TypeDestroyer.destroyType(aas)));
	
		// Create AAS registry
		IAASRegistryService registry = new PreconfiguredRegistry();
		// Create AAS Descriptor
		AASDescriptor aasDescriptor = new AASDescriptor(aasId, IdentifierType.URI, "/aas");
		// Create Submodel Descriptor
		SubmodelDescriptor smDescriptor = new SubmodelDescriptor(smId, IdentifierType.URI, "/aas/submodels/" + smId);
		// Add Submodel descriptor to aas descriptor
		aasDescriptor.addSubmodelDescriptor(smDescriptor);

		registry.register(new ModelUrn(aasId), aasDescriptor);

		// Create connector provider stub, map address to provider
		ConnectorProviderStub connectorProvider = new ConnectorProviderStub();
		connectorProvider.addMapping(aasDescriptor.getFirstEndpoint(), provider);
		connectorProvider.addMapping(smDescriptor.getFirstEndpoint(), provider);

	// Create connection manager using the dummy
		ConnectedAssetAdministrationShellManager manager = new ConnectedAssetAdministrationShellManager(registry,
				connectorProvider);

	// Create ConnectedAssetAdministrationShell
		connectedAAS = manager.retrieveAAS(new ModelUrn(aasId));
	}

	/**
	 * Tests the getId() function
	 */
	@Test
	public void testGetId() {
		assertEquals(aasId, connectedAAS.getId());
	}

	/**
	 * Tests retrieving the contained SubModels
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetSubmodel() throws Exception {
		// Check if the number of SubModels is as expected
		assertEquals(1, connectedAAS.getSubModels().size());

		// Check if the contained SubModel id is as expected
		assertTrue(connectedAAS.getSubModels().containsKey(smId));

		// Check if the submodel has been retrieved correctly
		ISubModel sm = connectedAAS.getSubModels().get(smId);
		ISingleProperty prop = (ISingleProperty) sm.getDataElements().get(propId);
		assertEquals(propVal, prop.get());
	}
}