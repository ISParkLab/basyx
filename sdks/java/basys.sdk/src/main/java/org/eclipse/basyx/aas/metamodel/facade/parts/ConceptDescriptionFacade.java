package org.eclipse.basyx.aas.metamodel.facade.parts;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.basyx.aas.metamodel.api.parts.IConceptDescription;
import org.eclipse.basyx.aas.metamodel.map.parts.ConceptDescription;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.IAdministrativeInformation;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.submodel.metamodel.facade.qualifier.ReferableFacade;
import org.eclipse.basyx.submodel.metamodel.map.identifier.Identifier;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.AdministrativeInformation;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Description;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.HasDataSpecification;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Identifiable;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Referable;

/**
 * Facade providing access to a map containing the ConceptDescription structure
 * 
 * @author rajashek
 *
 */

public class ConceptDescriptionFacade implements IConceptDescription {

	private Map<String, Object> map;

	public ConceptDescriptionFacade(Map<String, Object> map) {
		super();
		this.map = map;
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
	public IAdministrativeInformation getAdministration() {
		return (IAdministrativeInformation) map.get(Identifiable.ADMINISTRATION);
	}

	@Override
	public IIdentifier getIdentification() {
		return (IIdentifier) map.get(Identifiable.IDENTIFICATION);
	}

	public void setAdministration(String version, String revision) {
		map.put(Identifiable.ADMINISTRATION, new AdministrativeInformation(version, revision));

	}

	public void setIdentification(String idType, String id) {
		map.put(Identifiable.IDENTIFICATION, new Identifier(idType, id));

	}

	@Override
	@SuppressWarnings("unchecked")
	public HashSet<String> getisCaseOf() {
		return (HashSet<String>) map.get(ConceptDescription.ISCASEOF);
	}

	public void setIscaseOf(Set<String> ref) {
		map.put(ConceptDescription.ISCASEOF, ref);

	}

	@Override
	public String getIdShort() {
		return (String) map.get(Referable.IDSHORT);
	}

	@Override
	public String getCategory() {
		return (String) map.get(Referable.CATEGORY);
	}

	@Override
	public Description getDescription() {
		return new ReferableFacade(map).getDescription();
	}

	@Override
	public IReference getParent() {
		return (IReference) map.get(Referable.PARENT);
	}

	public void setIdShort(String idShort) {
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
