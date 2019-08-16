package org.eclipse.basyx.regression.sqlprovider.tests;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.basyx.aas.backend.connector.http.HTTPConnectorProvider;
import org.eclipse.basyx.regression.support.directory.ComponentsTestsuiteDirectory;
import org.eclipse.basyx.regression.support.server.context.ComponentsRegressionContext;
import org.eclipse.basyx.testsuite.support.backend.servers.AASHTTPServerResource;
import org.eclipse.basyx.vab.core.VABConnectionManager;
import org.eclipse.basyx.vab.core.proxy.VABElementProxy;
import org.junit.ClassRule;
import org.junit.Test;



/**
 * Test SQL queries
 * 
 * @author kuhn
 *
 */
public class SQLQueriesTest {

	
	/**
	 * Store HTTP asset administration shell manager backend
	 */
	protected VABConnectionManager connManager = new VABConnectionManager(new ComponentsTestsuiteDirectory(), new HTTPConnectorProvider());

	/** 
	 * Makes sure Tomcat Server is started
	 */
	@ClassRule
	public static AASHTTPServerResource res = new AASHTTPServerResource(new ComponentsRegressionContext());
	
	
	/**
	 * Test basic queries
	 */
	@Test
	public void test() throws Exception {

		// Connect to sub model "CfgFileTestAAS"
		VABElementProxy connSubModel = this.connManager.connectToVABElement("SQLTestSubmodel");

		
		// Get sub model
		Object value0A = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel");
		System.out.println("***Value:"+value0A);

		
		// Get properties
		Object value0B = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties");
		System.out.println("***Value:"+value0B);

		
		// Get property value
		Object value1 = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames/value");
		System.out.println("***Value:"+value1);

		// Get property value with meta data
		Object value1a = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames");
		System.out.println("***Value with meta data:"+value1a);

		
		// Create a new property
		// - HashMap that contains new table line
		Map<String, Object> newTableLine = new HashMap<>();
			newTableLine.put("sensorname", "VS_0003");
			newTableLine.put("sensorid",   "033542");
		// - Insert line into table
		connSubModel.createElement("/aas/submodels/SQLTestSubmodel/properties/sensorNames/value", newTableLine);
		
		// Get property value again
		Object value2 = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames/value");
		System.out.println("Value2:"+value2);

		Object value2a = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames");
		System.out.println("Value2a:"+value2a);

		
		// Update property value
		// - Here this adds a new value into the table
		// - Collection that contains call values
		Map<String, Object> updatedTableLine = new HashMap<>();
			updatedTableLine.put("sensorname", "VS_0004");
			updatedTableLine.put("sensorid", "033542");
		// - Update table line
		connSubModel.updateElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames/value", updatedTableLine);

		// Get property value again
		Object value3 = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames/value");
		System.out.println("Value3:"+value3);

		
		// Delete property with ID 033542
		// - Map that contains call values
		Map<String, Object> removedTableLine = new HashMap<>();
			removedTableLine.put("sensorid", "033542");
		// - Delete sensor from table
		connSubModel.deleteElement("/aas/submodels/SQLTestSubmodel/properties/sensorNames/value", removedTableLine);
		
		// Get property value again
		Object value4 = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames/value");
		System.out.println("Value4:"+value4);

		
		
		// Get property meta data value
		Object value5 = connSubModel.readElementValue("/aas/submodels/SQLTestSubmodel/properties/sensorNames/category");
		System.out.println("Value5:"+value5);
	}
}