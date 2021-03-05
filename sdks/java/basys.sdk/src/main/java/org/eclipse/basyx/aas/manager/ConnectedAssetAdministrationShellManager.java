/**
 * 
 */
package org.eclipse.basyx.aas.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.basyx.aas.aggregator.proxy.AASAggregatorProxy;
import org.eclipse.basyx.aas.aggregator.restapi.AASAggregatorProvider;
import org.eclipse.basyx.aas.manager.api.IAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.connected.ConnectedAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.AASDescriptor;
import org.eclipse.basyx.aas.metamodel.map.descriptor.SubmodelDescriptor;
import org.eclipse.basyx.aas.registration.api.IAASRegistryService;
import org.eclipse.basyx.submodel.metamodel.api.ISubmodel;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.connected.ConnectedSubmodel;
import org.eclipse.basyx.submodel.metamodel.map.Submodel;
import org.eclipse.basyx.submodel.restapi.SubmodelProvider;
import org.eclipse.basyx.vab.exception.FeatureNotImplementedException;
import org.eclipse.basyx.vab.factory.java.ModelProxyFactory;
import org.eclipse.basyx.vab.modelprovider.VABElementProxy;
import org.eclipse.basyx.vab.modelprovider.VABPathTools;
import org.eclipse.basyx.vab.modelprovider.api.IModelProvider;
import org.eclipse.basyx.vab.protocol.api.IConnectorProvider;
import org.eclipse.basyx.vab.protocol.http.connector.HTTPConnectorProvider;

/**
 * Implement a AAS manager backend that communicates via HTTP/REST<br />
 * <br />
 * 
 * @author kuhn, schnicke
 * 
 */
public class ConnectedAssetAdministrationShellManager implements IAssetAdministrationShellManager {

	protected IAASRegistryService aasDirectory;
	protected IConnectorProvider connectorProvider;
	protected ModelProxyFactory proxyFactory;

	/**
	 * Creates a manager assuming an HTTP connection
	 * 
	 * @param directory
	 */
	public ConnectedAssetAdministrationShellManager(IAASRegistryService directory) {
		this(directory, new HTTPConnectorProvider());
	}

	/**
	 * @param networkDirectoryService
	 * @param providerProvider
	 */
	public ConnectedAssetAdministrationShellManager(IAASRegistryService directory,
			IConnectorProvider provider) {
		this.aasDirectory = directory;
		this.connectorProvider = provider;
		this.proxyFactory = new ModelProxyFactory(provider);
	}

	@Override
	public ISubmodel retrieveSubmodel(IIdentifier aasId, IIdentifier smId) {
		// look up SM descriptor in the registry
		SubmodelDescriptor smDescriptor = aasDirectory.lookupSubmodel(aasId, smId);

		// get address of the submodel descriptor
		String addr = smDescriptor.getFirstEndpoint();

		// Return a new VABElementProxy
		return new ConnectedSubmodel(proxyFactory.createProxy(addr));
	}

	@Override
	public ConnectedAssetAdministrationShell retrieveAAS(IIdentifier aasId) {
		VABElementProxy proxy = getAASProxyFromId(aasId);
		return new ConnectedAssetAdministrationShell(proxy);
	}

	@Override
	public Map<String, ISubmodel> retrieveSubmodels(IIdentifier aasId) {
		AASDescriptor aasDesc = aasDirectory.lookupAAS(aasId);
		Collection<SubmodelDescriptor> smDescriptors = aasDesc.getSubmodelDescriptors();
		Map<String, ISubmodel> submodels = new HashMap<>();
		for (SubmodelDescriptor smDesc : smDescriptors) {
			String smEndpoint = smDesc.getFirstEndpoint();
			String smIdShort = smDesc.getIdShort();
			VABElementProxy smProxy = proxyFactory.createProxy(smEndpoint);
			ConnectedSubmodel connectedSM = new ConnectedSubmodel(smProxy);
			submodels.put(smIdShort, connectedSM);
		}
		return submodels;
	}

	private VABElementProxy getAASProxyFromId(IIdentifier aasId) {
		// Lookup AAS descriptor
		AASDescriptor aasDescriptor = aasDirectory.lookupAAS(aasId);

		// Get AAS address from AAS descriptor
		String addr = aasDescriptor.getFirstEndpoint();

		// Return a new VABElementProxy
		return proxyFactory.createProxy(addr);
	}

	@Override
	public Collection<IAssetAdministrationShell> retrieveAASAll() {
		throw new FeatureNotImplementedException();
	}

	@Override
	public void deleteAAS(IIdentifier id) {
		// Lookup AAS descriptor
		AASDescriptor aasDescriptor = aasDirectory.lookupAAS(id);

		// Get AAS address from AAS descriptor
		String addr = aasDescriptor.getFirstEndpoint();

		// Address ends in "/aas", has to be stripped for removal
		addr = VABPathTools.stripSlashes(addr);
		addr = addr.substring(0, addr.length() - "/aas".length());

		// Delete from server
		proxyFactory.createProxy(addr).deleteValue("");

		// Delete from Registry
		aasDirectory.delete(id);

		// TODO: How to handle submodels -> Lifecycle needs to be clarified
	}

	@Override
	public void createSubmodel(IIdentifier aasId, Submodel submodel) {
		
		// Push the SM to the server using the ConnectedAAS

		retrieveAAS(aasId).addSubmodel(submodel);
		
		// Lookup AAS descriptor
		AASDescriptor aasDescriptor = aasDirectory.lookupAAS(aasId);

		// Get aas endpoint
		String addr = aasDescriptor.getFirstEndpoint();
		
		// Register the SM
		String smEndpoint = VABPathTools.concatenatePaths(addr, AssetAdministrationShell.SUBMODELS, submodel.getIdShort(), SubmodelProvider.SUBMODEL);
		aasDirectory.register(aasId, new SubmodelDescriptor(submodel, smEndpoint));
	}

	@Override
	public void deleteSubmodel(IIdentifier aasId, IIdentifier submodelId) {
		IAssetAdministrationShell shell = retrieveAAS(aasId);
		shell.removeSubmodel(submodelId);

		aasDirectory.delete(aasId, submodelId);
	}

	@Override
	public void createAAS(AssetAdministrationShell aas, String endpoint) {
		endpoint = VABPathTools.stripSlashes(endpoint);
		if (!endpoint.endsWith(AASAggregatorProvider.PREFIX)) {
			endpoint += "/" + AASAggregatorProvider.PREFIX;
		}

		IModelProvider provider = connectorProvider.getConnector(endpoint);
		AASAggregatorProxy proxy = new AASAggregatorProxy(provider);
		proxy.createAAS(aas);
		try {

			String combinedEndpoint = VABPathTools.concatenatePaths(endpoint, URLEncoder.encode(aas.getIdentification().getId(), "UTF-8"), "aas");
			aasDirectory.register(new AASDescriptor(aas, combinedEndpoint));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding failed. This should never happen");
		}
	}
}
