package org.eclipse.basyx.testsuite.regression.aas.gateways;

import static org.junit.Assert.assertTrue;

import org.eclipse.basyx.aas.api.resources.basic.IAssetAdministrationShell;
import org.eclipse.basyx.aas.api.resources.basic.ICollectionProperty;
import org.eclipse.basyx.aas.api.resources.basic.ISubModel;
import org.eclipse.basyx.aas.backend.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.backend.connector.http.HTTPConnector;
import org.eclipse.basyx.testsuite.support.gateways.common.directory.GatewayTestsuiteDirectoryLine1IESE;
import org.junit.jupiter.api.Test;




/**
 * AAS gateways test case
 * 
 * Connect BaSys asset administration shells across networks/technologies
 * 
 * Network hierarchy
 * - iese.fraunhofer.de
 *   - office
 *     - manufacturing_controller
 *     - product_database
 *       + products
 * - manufacturing.de
 *   - line2
 *     - device2
 *       + description
 *       + status
 *     - line2int
 *       - device10
 *         + description
 *         + status
 * 
 * 
 * Test case: Connect from office.iese.fraunhofer to device2.line2.manufacturing.de via http-rest web services
 * 
 * @author pschorn
 *
 */
class TestAASGatewaysCollection_Line2FromLine1 {
	

	/**
	 * Store HTTP asset administration shell manager backend for office.iese.fraunhofer.de
	 */
	protected ConnectedAssetAdministrationShellManager aasManager = new ConnectedAssetAdministrationShellManager(new GatewayTestsuiteDirectoryLine1IESE(), new HTTPConnector());
	
	
	
	/**
	 * Run JUnit test case
	 */
	@Test
	void runTest() throws Exception {

		// Connect to AAS with ID "aas.device2.line2.manufacturing.de"
		// - Retrieve connected AAS from AAS ID
		IAssetAdministrationShell tempsensorAAS1 = this.aasManager.retrieveAAS("aas.device2.line2.manufacturing.de");
		
		// Connect to sub models
		ISubModel submodel1 = tempsensorAAS1.getSubModels().get("description");
		
		// Connect to sub model property and create value
		((ICollectionProperty) submodel1.getProperties().get("testCollection")).set(92); // we know testCollection is currently [5,42]
		Integer ninetytwo = (Integer) ((ICollectionProperty) submodel1.getProperties().get("testCollection")).get(2); 
		
		// Check if value has been added
		assertTrue(ninetytwo.equals(92));
		
		// delete value
		((ICollectionProperty) submodel1.getProperties().get("testCollection")).remove(92); // we know testCollection is currently [5,42,92]
		Integer null_ = (Integer) ((ICollectionProperty) submodel1.getProperties().get("testCollection")).get(2);
		
		// Check if value has been deleted
		assertTrue(null_==null);
	}
}

