package org.eclipse.basyx.regression.cfgprovider.tests;

import static org.junit.Assert.assertTrue;

import org.eclipse.basyx.aas.backend.connector.http.HTTPConnectorProvider;
import org.eclipse.basyx.regression.support.directory.ComponentsTestsuiteDirectory;
import org.eclipse.basyx.vab.VABConnectionManager;
import org.eclipse.basyx.vab.proxy.VABElementProxy;
import org.junit.jupiter.api.Test;



/**
 * Test queries to CFG file provider
 * 
 * @author kuhn
 *
 */
class TestCFGProvider {

	
	/**
	 * VAB connection manager backend
	 */
	protected VABConnectionManager connManager = new VABConnectionManager(new ComponentsTestsuiteDirectory(), new HTTPConnectorProvider());

	
	
	/**
	 * Test basic queries
	 */
	@Test
	void test() throws Exception {

		// Connect to sub model "CfgFileTestAAS"
		VABElementProxy connSubModel = this.connManager.connectToVABElement("CfgFileTestAAS");

		
		// Get property value
		Object value1 = connSubModel.readElementValue("/aas/submodels/sampleCFG/properties/cfgProperty1/value");
		assertTrue(value1.equals("exampleStringValue"));

		// Get property value
		Object value2 = connSubModel.readElementValue("/aas/submodels/sampleCFG/properties/cfgProperty2/value");
		assertTrue(value2.equals("12"));

		// Get property value
		Object value3 = connSubModel.readElementValue("/aas/submodels/sampleCFG/properties/cfgProperty3/value");
		assertTrue(value3.equals("45.8"));
	}
}