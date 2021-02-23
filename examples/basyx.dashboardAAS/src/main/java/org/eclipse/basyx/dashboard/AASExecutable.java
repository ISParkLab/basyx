package org.eclipse.basyx.dashboard;

import java.util.ArrayList;

import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.AASDescriptor;
import org.eclipse.basyx.aas.metamodel.map.descriptor.SubmodelDescriptor;
import org.eclipse.basyx.aas.metamodel.map.parts.Asset;
import org.eclipse.basyx.aas.registration.api.IAASRegistryService;
import org.eclipse.basyx.aas.registration.proxy.AASRegistryProxy;
import org.eclipse.basyx.aas.restapi.AASModelProvider;
import org.eclipse.basyx.aas.restapi.VABMultiSubmodelProvider;
import org.eclipse.basyx.components.configuration.BaSyxContextConfiguration;
import org.eclipse.basyx.submodel.metamodel.map.SubModel;
import org.eclipse.basyx.submodel.restapi.SubModelProvider;
import org.eclipse.basyx.vab.modelprovider.api.IModelProvider;
import org.eclipse.basyx.vab.protocol.http.server.AASHTTPServer;
import org.eclipse.basyx.vab.protocol.http.server.BaSyxContext;
import org.eclipse.basyx.vab.protocol.http.server.VABHTTPInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executable for a very basic, dynamic AAS, that is meant to be used in an example about how
 * to integrate properties of "real" AAS in a dashboard.
 * 
 * @author espen
 *
 */
public class AASExecutable {
	private static final Logger logger = LoggerFactory.getLogger(AASExecutable.class);

	public static final String HOST = "dashboard-aas";
	public static final int PORT = 6400;
	public static final String REGISTRY_ENDPOINT = "http://registry:4000/registry/";

	public static void main(String[] args) throws Exception {
		// Use docker-compose health check for registry instead
		Thread.sleep(3000);

		logger.info("Creating AAS...");
		AssetAdministrationShell aas = new DashboardAssetAdministrationShell();
		Asset asset = new DashboardAsset();
		aas.setAsset(asset);
		SubModel sm = new DashboardSubmodel();
		aas.setConceptDictionary(new ArrayList<>());
		aas.addSubModel(sm);
		
		logger.info("Starting aas servlet...");
		createServlet(aas, sm);
		logger.info("Registering aas...");
		AASDescriptor descriptor = new AASDescriptor(aas, "http://" + HOST + ":" + PORT + "/aas");
		descriptor.addSubmodelDescriptor(
				new SubmodelDescriptor(sm, "http://" + HOST + ":" + PORT + "/aas/submodels/DashboardSubmodel/"));
		registerAAS(descriptor, REGISTRY_ENDPOINT);

		logger.info("Finished");
	}

	private static void registerAAS(AASDescriptor descriptor, String registryEndpoint) throws InterruptedException {
		IAASRegistryService registry = new AASRegistryProxy(registryEndpoint);
		// Quick & dirty, try to register until registry is up
		for (int i = 0; i < 10; i++) {
			try {
				registry.register(descriptor);
				break;
			} catch (Exception e) {
				Thread.sleep(2000);
			}
		}
	}

	private static void createServlet(AssetAdministrationShell aas, SubModel... submodels) {
		VABMultiSubmodelProvider provider = new VABMultiSubmodelProvider();
		provider.setAssetAdministrationShell(new AASModelProvider(aas));
		for (SubModel sm : submodels) {
			provider.addSubmodel(new SubModelProvider(sm));
		}
		BaSyxContextConfiguration config = new BaSyxContextConfiguration();
		config.loadFromDefaultSource();
		BaSyxContext context = config.createBaSyxContext();
		context.addServletMapping("/*", new VABHTTPInterface<IModelProvider>(provider));
        AASHTTPServer server = new AASHTTPServer(context);
		server.start();
	}
}