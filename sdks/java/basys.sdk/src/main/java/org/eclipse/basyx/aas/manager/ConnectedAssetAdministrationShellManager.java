/**
 * 
 */
package org.eclipse.basyx.aas.manager;

import java.util.Collection;

import org.eclipse.basyx.aas.manager.api.IAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.connected.ConnectedAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.AASDescriptor;
import org.eclipse.basyx.aas.metamodel.map.descriptor.ModelUrn;
import org.eclipse.basyx.aas.metamodel.map.descriptor.SubmodelDescriptor;
import org.eclipse.basyx.aas.registration.api.IAASRegistryService;
import org.eclipse.basyx.submodel.metamodel.api.ISubModel;
import org.eclipse.basyx.submodel.metamodel.connected.ConnectedSubModel;
import org.eclipse.basyx.submodel.metamodel.map.SubModel;
import org.eclipse.basyx.vab.exception.FeatureNotImplementedException;
import org.eclipse.basyx.vab.modelprovider.VABElementProxy;
import org.eclipse.basyx.vab.modelprovider.VABPathTools;
import org.eclipse.basyx.vab.protocol.api.IConnectorProvider;

/**
 * Implement a AAS manager backend that communicates via HTTP/REST<br />
 * <br />
 * 
 * @author kuhn, schnicke
 * 
 */
public class ConnectedAssetAdministrationShellManager implements IAssetAdministrationShellManager {

	protected IAASRegistryService aasDirectory;

	protected IConnectorProvider providerProvider;

	/**
	 * @param networkDirectoryService
	 * @param providerProvider
	 */
	public ConnectedAssetAdministrationShellManager(IAASRegistryService directory,
			IConnectorProvider provider) {
		this.aasDirectory = directory;
		this.providerProvider = provider;
	}

	@Override
	public ISubModel retrieveSubModel(ModelUrn aasUrn, String smid) {
		// look up AAS descriptor in the registry
		AASDescriptor aasDescriptor = aasDirectory.lookupAAS(aasUrn);

		// Get submodel descriptor from the aas descriptor
		SubmodelDescriptor smDescriptor = aasDescriptor.getSubModelDescriptor(smid);

		// get address of the submodel descriptor
		String addr = smDescriptor.getFirstEndpoint();

		// Return a new VABElementProxy
		VABElementProxy proxy = new VABElementProxy(VABPathTools.removeAddressEntry(addr),
				providerProvider.getConnector(addr));
		return new ConnectedSubModel(proxy);
	}

	@Override
	public ConnectedAssetAdministrationShell retrieveAAS(ModelUrn aasUrn) throws Exception {
		VABElementProxy proxy = getAASProxyFromURN(aasUrn);
		return new ConnectedAssetAdministrationShell(proxy, this);
	}

	@Override
	public void createAAS(AssetAdministrationShell aas, ModelUrn urn) {
		VABElementProxy proxy = getAASProxyFromURN(urn);

		proxy.createValue("/", aas);
	}

	private VABElementProxy getAASProxyFromURN(ModelUrn aasUrn) {
		// Lookup AAS descriptor
		AASDescriptor aasDescriptor = aasDirectory.lookupAAS(aasUrn);

		// Get AAD address from AAS descriptor
		String addr = aasDescriptor.getFirstEndpoint();

		// Return a new VABElementProxy
		return new VABElementProxy(VABPathTools.removeAddressEntry(addr), providerProvider.getConnector(addr));
	}

	@Override
	public Collection<IAssetAdministrationShell> retrieveAASAll() {
		throw new FeatureNotImplementedException();
	}

	@Override
	public void deleteAAS(String id) throws Exception {
		throw new FeatureNotImplementedException();
	}

	@Override
	public void createSubModel(ModelUrn aasUrn, SubModel submodel) {
		// Lookup AAS descriptor
		AASDescriptor aasDescriptor = aasDirectory.lookupAAS(aasUrn);

		// Get aas endpoint
		String addr = aasDescriptor.getFirstEndpoint();

		// Return a new VABElementProxy
		VABElementProxy proxy = new VABElementProxy(VABPathTools.removeAddressEntry(addr),
				providerProvider.getConnector(addr));

		// Create sm
		proxy.createValue(AssetAdministrationShell.SUBMODELS, submodel);
	}
}