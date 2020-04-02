package org.eclipse.basyx.regression.support.bundle;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.AASDescriptor;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IdentifierType;
import org.eclipse.basyx.submodel.metamodel.map.SubModel;
import org.eclipse.basyx.support.bundle.AASBundle;
import org.eclipse.basyx.support.bundle.AASBundleDescriptorFactory;
import org.eclipse.basyx.vab.modelprovider.VABPathTools;
import org.junit.Test;

/**
 * Tests the methods of AASBundleDescriptorFactory for their correctness
 * 
 * @author schnicke
 *
 */
public class TestAASBundleDescriptorFactory {
	@Test
	public void testDescriptorCreation() {
		String aasId = "aasId";
		AssetAdministrationShell shell = new AssetAdministrationShell();
		shell.setIdShort(aasId);

		String smId = "smId";
		SubModel sm = new SubModel();
		sm.setIdShort(smId);
		sm.setIdentification(IdentifierType.IRI, "aasIdIRI");

		AASBundle bundle = new AASBundle(shell, Collections.singleton(sm));
		
		String basePath = "http://localhost:4040/test";		
		AASDescriptor desc = AASBundleDescriptorFactory.createAASDescriptor(bundle, basePath);
		
		String aasPath = VABPathTools.concatenatePaths(basePath, aasId, "aas");
		String smPath = VABPathTools.concatenatePaths(aasPath, "submodels", sm.getIdShort(), "submodel");
		assertEquals(aasPath, desc.getFirstEndpoint());
		assertEquals(smPath, desc.getSubmodelDescriptorFromIdShort(smId).getFirstEndpoint());

	}

}
