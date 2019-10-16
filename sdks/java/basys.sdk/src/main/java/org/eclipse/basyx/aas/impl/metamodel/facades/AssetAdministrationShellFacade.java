package org.eclipse.basyx.aas.impl.metamodel.facades;

import java.util.Map;
import java.util.Set;

import org.eclipse.basyx.aas.api.metamodel.aas.IAssetAdministrationShell;
import org.eclipse.basyx.aas.api.metamodel.aas.ISubModel;
import org.eclipse.basyx.aas.api.metamodel.aas.parts.IConceptDictionary;
import org.eclipse.basyx.aas.api.metamodel.aas.parts.IView;
import org.eclipse.basyx.aas.api.metamodel.aas.reference.IReference;
import org.eclipse.basyx.aas.api.metamodel.aas.security.ISecurity;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.AssetAdministrationShell;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.identifier.Identifier;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.qualifier.AdministrativeInformation;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.qualifier.HasDataSpecification;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.qualifier.Identifiable;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.qualifier.Referable;

/**
 * Facade providing access to a map containing the AssetAdministrationShell
 * structure
 * 
 * @author rajashek
 *
 */
public class AssetAdministrationShellFacade implements IAssetAdministrationShell {

	private Map<String, Object> map;

	public AssetAdministrationShellFacade(Map<String, Object> map) {
		super();
		this.map = map;
	}

	public void setSecurity(ISecurity security) {
		map.put(AssetAdministrationShell.SECURITY, security);
	}

	@Override
	public ISecurity getSecurity() {
		return (ISecurity) map.get(AssetAdministrationShell.SECURITY);
	}

	public void setDerivedFrom(IReference derivedFrom) {
		map.put(AssetAdministrationShell.DERIVEDFROM, derivedFrom);
	}

	@Override
	public IReference getDerivedFrom() {
		return (IReference) map.get(AssetAdministrationShell.DERIVEDFROM);
	}

	public void setAsset(IReference asset) {
		map.put(AssetAdministrationShell.ASSET, asset);
	}

	@Override
	public IReference getAsset() {
		return (IReference) map.get(AssetAdministrationShell.ASSET);
	}

	@Override
	public void setSubModel(Set<IReference> submodels) {
		map.put(AssetAdministrationShell.SUBMODEL, submodels);

	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<IReference> getSubModel() {
		return (Set<IReference>) map.get(AssetAdministrationShell.SUBMODEL);
	}

	public void setViews(Set<IView> views) {
		map.put(AssetAdministrationShell.VIEWS, views);

	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<IView> getViews() {
		return (Set<IView>) map.get(AssetAdministrationShell.VIEWS);
	}

	public void setConceptDictionary(Set<IConceptDictionary> dictionaries) {
		map.put(AssetAdministrationShell.CONCEPTDICTIONARY, dictionaries);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<IConceptDictionary> getConceptDictionary() {
		return (Set<IConceptDictionary>) map.get(AssetAdministrationShell.CONCEPTDICTIONARY);
	}

	@Override
	public AdministrativeInformation getAdministration() {
		return (AdministrativeInformation) map.get(Identifiable.ADMINISTRATION);
	}

	@Override
	public Identifier getIdentification() {
		return (Identifier) map.get(Identifiable.IDENTIFICATION);
	}

	public void setAdministration(String version, String revision) {
		map.put(Identifiable.ADMINISTRATION, new AdministrativeInformation(version, revision));
	}

	public void setIdentification(String idType, String id) {
		map.put(Identifiable.IDENTIFICATION, new Identifier(idType, id));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<IReference> getDataSpecificationReferences() {
		return (Set<IReference>) map.get(HasDataSpecification.HASDATASPECIFICATION);
	}

	public void setDataSpecificationReferences(Set<IReference> ref) {
		map.put(HasDataSpecification.HASDATASPECIFICATION, ref);
	}

	@Override
	public String getId() {
		return (String) map.get(Referable.IDSHORT);
	}

	@Override
	public void setId(String id) {
		map.put(Referable.IDSHORT, id);
	}

	@Override
	public Map<String, ISubModel> getSubModels() {
		throw new RuntimeException("getSubModels on local copy is not supported");
	}

	@SuppressWarnings("unchecked")
	public Set<ISubModel> getSubModelsHack() {
		return (Set<ISubModel>) map.get(AssetAdministrationShell.SUBMODELS);
	}

	@Override
	public void addSubModel(ISubModel subModel) {
		throw new RuntimeException("addSubModel on local copy is not supported");
	}

	@Override
	public String getIdshort() {
		return (String) map.get(Referable.IDSHORT);
	}

	@Override
	public String getCategory() {
		return (String) map.get(Referable.CATEGORY);
	}

	@Override
	public String getDescription() {
		return (String) map.get(Referable.DESCRIPTION);
	}

	@Override
	public IReference getParent() {
		return (IReference) map.get(Referable.PARENT);
	}

	public void setIdshort(String idShort) {
		map.put(Referable.IDSHORT, idShort);
	}

	public void setCategory(String category) {
		map.put(Referable.CATEGORY, category);
	}

	public void setDescription(String description) {
		map.put(Referable.DESCRIPTION, description);
	}

	public void setParent(IReference obj) {
		map.put(Referable.PARENT, obj);
	}

}
