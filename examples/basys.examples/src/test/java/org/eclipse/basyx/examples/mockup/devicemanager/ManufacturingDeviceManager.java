package org.eclipse.basyx.examples.mockup.devicemanager;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.basyx.aas.api.modelurn.ModelUrn;
import org.eclipse.basyx.aas.api.registry.AASHTTPRegistryProxy;
import org.eclipse.basyx.aas.backend.connector.http.HTTPConnectorProvider;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.SubModel;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.descriptor.AASDescriptor;
import org.eclipse.basyx.components.configuration.CFGBaSyxProtocolType;
import org.eclipse.basyx.components.devicemanager.TCPDeviceManagerComponent;
import org.eclipse.basyx.examples.support.directory.ExamplesPreconfiguredDirectory;
import org.eclipse.basyx.vab.core.VABConnectionManager;
import org.eclipse.basyx.vab.core.proxy.VABElementProxy;



/**
 * Example manufacturing device manager code
 * 
 * This example code illustrates a basic device manager component. It implements the interaction between a device and the BaSyx infrastructure.
 * This code is for example deployed on the device (in case of availability of a Java runtime environment) or to an explicit connector device.
 * The Asset Administration Shell is not kept on the device, but transferred to an AAS server during registration. This ensures its presence also
 * if the device itself is not available, e.g. due to a failure. Important asset data, such as manufacturer, and support contacts remain available
 * in this case.
 * 
 * This code implements the following:
 * - Registration of device the AAS and sub models with the BaSyx infrastructure
 * - Updating of sub model properties to reflect the device status
 * - TCP connection to legacy device
 * 
 * 
 * @author kuhn
 *
 */
public class ManufacturingDeviceManager extends TCPDeviceManagerComponent {

	
	/**
	 * AAS server connection
	 */
	protected VABElementProxy aasServerConnection = null;






	/**
	 * Constructor
	 */
	public ManufacturingDeviceManager(int port) {
		// Invoke base constructor
		super(port);
		
		
		// Configure this device manager
		configure()
			.registryURL("http://localhost:8080/basys.examples/Components/Directory/SQL")
			.connectionManagerType(CFGBaSyxProtocolType.HTTP)
				.directoryService(new ExamplesPreconfiguredDirectory())
			.end();
		
		// configure()
		//   .registryURL()
		//   .connectionManagerDirectory(new ExamplesPreconfiguredDirectory())
		//   .connectionManagerProtocol(HTTP)
		//   .AASServerObjectID(...)
		//   .addAASAAS()
		//   	.whateverAASProperty()
		//   	.addSubmodel()
		//			.property()
		//			.endSubModel()
		//		.end();
		
		
		// configure(Map<>...)
		
		// Set registry that will be used by this service
		setRegistry(new AASHTTPRegistryProxy("http://localhost:8080/basys.examples/Components/Directory/SQL"));
		
		
		// Set service connection manager and create AAS server connection
		setConnectionManager(new VABConnectionManager(new ExamplesPreconfiguredDirectory(), new HTTPConnectorProvider()));
		// - Create AAS server connection
		aasServerConnection = getConnectionManager().connectToHTTPVABElement("AASServer", "/aas/submodels/aasRepository/");
		
		
		// Set AAS server VAB object ID, AAS server URL, and AAS server path prefix
		setAASServerObjectID("AASServer");
		setAASServerURL("http://localhost:8080/basys.examples/Components/BaSys/1.0/aasServer");
		setAASServerPathPrefix("/aas/submodels/aasRepository/");
	}



	/**
	 * Initialize the device, and register it with the backend
	 */
	@Override 
	public void start() {
		// Base implementation
		super.start();
		
		// Create the device AAS and sub model structure
		createDeviceAASAndSubModels();
		
		// Register AAS and sub model descriptors in directory (push AAS descriptor to server)
		getRegistry().register(lookupURN("AAS"), getAASDescriptor());
	}
	
	
	/**
	 * Get AAS descriptor for managed device
	 */
	@Override 
	protected AASDescriptor getAASDescriptor() {
		// Create AAS and sub model descriptors
		AASDescriptor aasDescriptor = createAASDescriptorURI(lookupURN("AAS"));
		addSubModelDescriptorURI(aasDescriptor, lookupURN("Status"));
		addSubModelDescriptorURI(aasDescriptor, lookupURN("Controller"));
		
		// Return AAS and sub model descriptors
		return aasDescriptor;
	}

	
	
	/**
	 * Create the device AAS and sub model structure
	 */
	@SuppressWarnings("unchecked")
	protected void createDeviceAASAndSubModels() {
		
		// Register URNs of managed VAB objects
		addShortcut("AAS",        new ModelUrn("urn:de.FHG:devices.es.iese:aas:1.0:3:x-509#001"));
		addShortcut("Status",     new ModelUrn("urn:de.FHG:devices.es.iese:statusSM:1.0:3:x-509#001"));
		addShortcut("Controller", new ModelUrn("urn:de.FHG:devices.es.iese:controllerSM:1.0:3:x-509#001"));
		

		// Create device AAS
		AssetAdministrationShell aas = new AssetAdministrationShell();
		// - Populate AAS
		aas.setId("DeviceIDShort");
		// - Transfer device AAS to server
		aasServerConnection.createValue(lookupURN("AAS").toString(), aas);

	
		// The device also brings a sub model structure with an own ID that is being pushed on the server
		// - Create generic sub model and add properties
		SubModel statusSM = new SubModel()
		//   - Property status: indicate device status
				.putPath("properties/status", "offline")
		//   - Property statistics: export invocation statistics for every service
		//     - invocations: indicate total service invocations. Properties are not persisted in this example,
		//                    therefore we start counting always at 0.
				.putPath("properties/statistics/default/invocations", 0);
		// - Transfer device sub model to server
		aasServerConnection.createValue(lookupURN("Status").toString(), statusSM);

		
		// The device also brings a sub model structure with an own ID that is being pushed on the server
		// - Create generic sub model 
		SubModel controllerSM = new SubModel();
		//   - Create sub model contents manually
		Map<String, Object> listOfControllers = new HashMap<>();
		((Map<String, Object>) controllerSM.get(SubModel.PROPERTIES)).put("controllers", listOfControllers);
		// - Transfer device sub model to server
		aasServerConnection.createValue(lookupURN("Controller").toString(), controllerSM);
	}


	
	
	
	
	/**
	 * Received a string from network
	 */
	@Override
	public void onReceive(byte[] rxData) {
		// Do not process null values
		if (rxData == null) return;
		
		// Convert received data to string
		String rxStr = new String(rxData); 
		// - Trim string to remove possibly trailing and leading white spaces
		rxStr = rxStr.trim();
		
		// Check what was being received. This check is performed based on a prefix that he device has to provide);
		// - Update of device status
		if (hasPrefix(rxStr, "status:")) aasServerConnection.setModelPropertyValue(lookupURN("Status").getEncodedURN()+"/properties/status", removePrefix(rxStr, "status"));
		// - Device indicates service invocation
		if (hasPrefix(rxStr, "invocation:")) {
			// Start of process
			if (hasPrefix(rxStr, "invocation:start")) {
				// Read and increment invocation counter
				int invocations = (int) aasServerConnection.getModelPropertyValue(lookupURN("Status").getEncodedURN()+"/properties/statistics/default/invocations");
				aasServerConnection.setModelPropertyValue(lookupURN("Status").getEncodedURN()+"/properties/statistics/default/invocations", ++invocations);
			} 
			// End of process
			if (hasPrefix(rxStr, "invocation:end")) {
				// Do nothing for now
			}
		}
	}
}
