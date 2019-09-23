package org.eclipse.basyx.testsuite.regression.aas.backend.connected.property;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.eclipse.basyx.aas.api.metamodel.aas.submodelelement.property.ISingleProperty;
import org.eclipse.basyx.aas.backend.connected.aas.submodelelement.property.ConnectedSingleProperty;
import org.eclipse.basyx.aas.backend.provider.VirtualPathModelProvider;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.submodelelement.property.SingleProperty;
import org.eclipse.basyx.testsuite.support.vab.TypeDestroyer;
import org.eclipse.basyx.testsuite.support.vab.stub.VABConnectionManagerStub;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests if a ConnectedSingleProperty can be created and used correctly
 * 
 * @author schnicke
 *
 */
public class TestConnectedSingleProperty {

	ISingleProperty prop;
	private static final int VALUE = 10;

	@Before
	public void build() {
		// Create PropertySingleValued containing the simple value
		SingleProperty propertyMeta = new SingleProperty(VALUE);
		Map<String, Object> destroyType = TypeDestroyer.destroyType(propertyMeta);
		prop = new ConnectedSingleProperty(new VABConnectionManagerStub(new VirtualPathModelProvider(destroyType)).connectToVABElement(""));
	}

	/**
	 * Tests getting the value
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		int val = (int) prop.get();
		assertEquals(VALUE, val);
	}

	/**
	 * Tests setting the value
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSet() throws Exception {
		prop.set(123);
		int val = (int) prop.get();
		assertEquals(123, val);
	}
}
