package org.eclipse.basyx.testsuite.regression.aas.backend.connected.facade;

import static org.junit.Assert.assertEquals;

import org.eclipse.basyx.aas.backend.connected.facades.ConnectedHasKindFacade;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.qualifier.haskind.HasKind;
import org.eclipse.basyx.testsuite.support.vab.stub.VABConnectionManagerStub;
import org.eclipse.basyx.vab.core.VABConnectionManager;
import org.eclipse.basyx.vab.provider.hashmap.VABHashmapProvider;
import org.junit.Before;
import org.junit.Test;

public class TestConnectedHasKindFacade {
	
	
	HasKind local;
	ConnectedHasKindFacade remote;
	@Before
	public void build() {
		local = new HasKind("Kind");
		  
		// Create a dummy connection manager containing the created SubModel map
		VABConnectionManager manager = new VABConnectionManagerStub(new VABHashmapProvider(local));

		// Create the ConnectedSubModel based on the manager stub
		remote = new ConnectedHasKindFacade("", manager.connectToVABElement(""));
	}
	
	@Test
	public void test() {
	
		assertEquals(local.getHasKindReference(), remote.getHasKindReference());
	}

}