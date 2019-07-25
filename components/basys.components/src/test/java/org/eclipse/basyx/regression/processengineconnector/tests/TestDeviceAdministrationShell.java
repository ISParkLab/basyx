package org.eclipse.basyx.regression.processengineconnector.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.eclipse.basyx.aas.api.resources.IOperation;
import org.eclipse.basyx.aas.api.resources.ISubModel;
import org.eclipse.basyx.aas.backend.connected.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.backend.connected.aas.ConnectedAssetAdministrationShell;
import org.eclipse.basyx.aas.backend.connector.http.HTTPConnectorProvider;
import org.eclipse.basyx.regression.support.processengine.SetupHTTResource;
import org.eclipse.basyx.testsuite.support.vab.stub.DirectoryServiceStub;
import org.eclipse.basyx.vab.core.VABConnectionManager;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;


/**
 * 
 * 
 * @author zhangzai
 *
 */
public class TestDeviceAdministrationShell {
	private  ConnectedAssetAdministrationShellManager manager;
	ConnectedAssetAdministrationShell connectedAAS;
	/**
	 * Makes sure Tomcat Server is started
	 */
	@ClassRule
	public static SetupHTTResource res = new SetupHTTResource();
	@Before
	public void setupConnection() {
		
		
		//set-up the administration shell manager to create connected aas
		 manager = new ConnectedAssetAdministrationShellManager(new VABConnectionManager(new DirectoryServiceStub()
				 																							.addMapping("coilcar", "http://localhost:8080/basys.sdk/Testsuite/coilcar/")
				 																							.addMapping("submodel1", "http://localhost:8080/basys.sdk/Testsuite/coilcar/"),
				 																		 new HTTPConnectorProvider()));
		
		// create the connected AAS using the manager
		try {
			 connectedAAS = (ConnectedAssetAdministrationShell) manager.retrieveAAS("coilcar");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void operationTest() throws Exception {
		assertEquals(1, connectedAAS.getSubModels().size());

		// Check if the contained SubModel id is as expected
		assertTrue(connectedAAS.getSubModels().containsKey("submodel1"));
		ISubModel sm = connectedAAS
				.getSubModels()
				.get("submodel1");
		
		Map<String, IOperation> operations = sm.getOperations();
		assertEquals(2, operations.size());
		
		IOperation op1 = operations.get("liftTo");
		op1.invoke(5);
		IOperation op2 = operations.get("moveTo");
		op2.invoke(55);
	}
}
