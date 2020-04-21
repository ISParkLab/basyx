package org.eclipse.basyx.aas.metamodel.connected;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.basyx.aas.manager.ConnectedAssetAdministrationShellManager;
import org.eclipse.basyx.aas.metamodel.api.IAssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.api.parts.IConceptDictionary;
import org.eclipse.basyx.aas.metamodel.api.parts.IView;
import org.eclipse.basyx.aas.metamodel.api.parts.asset.IAsset;
import org.eclipse.basyx.aas.metamodel.api.security.ISecurity;
import org.eclipse.basyx.aas.metamodel.map.AssetAdministrationShell;
import org.eclipse.basyx.aas.metamodel.map.descriptor.SubmodelDescriptor;
import org.eclipse.basyx.aas.metamodel.map.parts.Asset;
import org.eclipse.basyx.aas.metamodel.map.parts.ConceptDictionary;
import org.eclipse.basyx.aas.metamodel.map.parts.View;
import org.eclipse.basyx.aas.metamodel.map.security.Security;
import org.eclipse.basyx.submodel.metamodel.api.ISubModel;
import org.eclipse.basyx.submodel.metamodel.api.dataspecification.IEmbeddedDataSpecification;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.IAdministrativeInformation;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.submodel.metamodel.connected.ConnectedSubModel;
import org.eclipse.basyx.submodel.metamodel.connected.ConnectedVABModelMap;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.HasDataSpecification;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Identifiable;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Referable;
import org.eclipse.basyx.submodel.metamodel.map.reference.Reference;
import org.eclipse.basyx.submodel.metamodel.map.reference.ReferenceHelper;
import org.eclipse.basyx.vab.modelprovider.VABElementProxy;

/**
 * "Connected" implementation of IAssetAdministrationShell
 * 
 * @author rajashek, Zai Zhang
 *
 */
public class ConnectedAssetAdministrationShell extends ConnectedVABModelMap<Object> implements IAssetAdministrationShell {

	ConnectedAssetAdministrationShellManager manager;

	/**
	 * Constructor creating a ConnectedAAS pointing to the AAS represented by proxy
	 * and path
	 * 
	 * @param path
	 * @param proxy
	 * @param manager
	 */
	public ConnectedAssetAdministrationShell(VABElementProxy proxy, ConnectedAssetAdministrationShellManager manager) {
		super(proxy);
		this.manager = manager;
	}

	/**
	 * Copy constructor, allowing to create a ConnectedAAS pointing to the same AAS
	 * as <i>shell</i>
	 * 
	 * @param shell
	 */
	public ConnectedAssetAdministrationShell(ConnectedAssetAdministrationShell shell) {
		super(shell.getProxy());
	}

	@Override
	public IAdministrativeInformation getAdministration() {
		return Identifiable.createAsFacade(getElem()).getAdministration();
	}

	@Override
	public IIdentifier getIdentification() {
		return Identifiable.createAsFacade(getElem()).getIdentification();
	}

	@Override
	public Collection<IReference> getDataSpecificationReferences() {
		return HasDataSpecification.createAsFacade(getElem()).getDataSpecificationReferences();
	}

	@Override
	public Collection<IEmbeddedDataSpecification> getEmbeddedDataSpecifications() {
		return HasDataSpecification.createAsFacade(getElem()).getEmbeddedDataSpecifications();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ISecurity getSecurity() {
		return Security.createAsFacade((Map<String, Object>) getElem().getPath(AssetAdministrationShell.SECURITY));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IReference getDerivedFrom() {
		return Reference.createAsFacade((Map<String, Object>) getElem().getPath(AssetAdministrationShell.DERIVEDFROM));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAsset getAsset() {
		return Asset.createAsFacade((Map<String, Object>) getElem().getPath(AssetAdministrationShell.ASSET));
	}

	@SuppressWarnings("unchecked")
	@Override

	public Collection<SubmodelDescriptor> getSubModelDescriptors() {
		Collection<Map<String, Object>> coll = (Collection<Map<String, Object>>) getElem()
				.getPath(AssetAdministrationShell.SUBMODELDESCRIPTORS);
		return coll.stream().map(SubmodelDescriptor::new).collect(Collectors.toSet());
	}

	@SuppressWarnings("unchecked")
	@Override

	public Collection<IView> getViews() {
		Collection<Map<String, Object>> coll = (Collection<Map<String, Object>>) getElem()
				.getPath(AssetAdministrationShell.VIEWS);
		return coll.stream().map(View::createAsFacade).collect(Collectors.toSet());
	}

	@SuppressWarnings("unchecked")
	@Override

	public Collection<IConceptDictionary> getConceptDictionary() {
		Collection<Map<String, Object>> set = (Collection<Map<String, Object>>) getElem()
				.getPath(AssetAdministrationShell.CONCEPTDICTIONARY);
		return set.stream().map(ConceptDictionary::createAsFacade).collect(Collectors.toSet());
	}


	@Override
	public Map<String, ISubModel> getSubModels() {
		Map<String, ISubModel> ret = new HashMap<>();

		Collection<SubmodelDescriptor> submodelDescriptors = getSubModelDescriptors();
		for (final SubmodelDescriptor submodelDescriptor : submodelDescriptors) {
			String id = submodelDescriptor.getIdShort();
			ret.put(id, new ConnectedSubModel(getProxy().getDeepProxy(AssetAdministrationShell.SUBMODELS + "/" + id)));
		}

		return ret;
	}

	@Override
	public void addSubModel(SubmodelDescriptor subModel) {
		getProxy().createValue("/aas/submodels", subModel);
	}

	@Override
	public String getIdShort() {
		return (String) getElem().get(Referable.IDSHORT);
	}

	@Override
	public String getCategory() {
		return (String) getElem().get(Referable.CATEGORY);
	}

	@Override
	public LangStrings getDescription() {
		return Referable.createAsFacade(getElem()).getDescription();
	}

	@SuppressWarnings("unchecked")
	@Override
	public IReference getParent() {
		return Reference.createAsFacade((Map<String, Object>) getElem().getPath(Referable.PARENT));
	}

	@Override
	public Collection<IReference> getSubmodelReferences() {
		return ReferenceHelper.transform(getElem().getPath(AssetAdministrationShell.SUBMODELS));
	}

	@SuppressWarnings("unchecked")
	@Override
	public IReference getAssetReference() {
		return Reference.createAsFacade((Map<String, Object>) getElem().getPath(AssetAdministrationShell.ASSETREF));
	}
}
