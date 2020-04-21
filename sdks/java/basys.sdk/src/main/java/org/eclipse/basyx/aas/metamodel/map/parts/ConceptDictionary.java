package org.eclipse.basyx.aas.metamodel.map.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.basyx.aas.metamodel.api.parts.IConceptDictionary;
import org.eclipse.basyx.submodel.metamodel.api.identifier.IIdentifier;
import org.eclipse.basyx.submodel.metamodel.api.parts.IConceptDescription;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.submodel.metamodel.api.reference.enums.KeyElements;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.LangStrings;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Referable;
import org.eclipse.basyx.submodel.metamodel.map.reference.Key;
import org.eclipse.basyx.submodel.metamodel.map.reference.Reference;
import org.eclipse.basyx.submodel.metamodel.map.reference.ReferenceHelper;
import org.eclipse.basyx.vab.model.VABModelMap;

/**
 * ConceptDictionary class as described in DAAS document
 * 
 * @author elsheikh, schnicke
 *
 */
public class ConceptDictionary extends VABModelMap<Object> implements IConceptDictionary {
	public static final String CONCEPTDESCRIPTION = "conceptDescription";

	// Extension of meta model to support local concept descriptions
	public static final String CONCEPTDESCRIPTIONS = "conceptDescriptions";

	/**
	 * Constructor
	 */
	public ConceptDictionary() {
		putAll(new Referable());
		put(CONCEPTDESCRIPTION, new ArrayList<IReference>());
		put(CONCEPTDESCRIPTIONS, new ArrayList<IConceptDescription>());
	}

	public ConceptDictionary(Collection<IReference> ref) {
		putAll(new Referable());
		put(CONCEPTDESCRIPTION, ref);
		put(CONCEPTDESCRIPTIONS, new ArrayList<IConceptDescription>());
	}

	/**
	 * Creates a ConceptDictionary object from a map
	 * 
	 * @param obj
	 *            a ConceptDictionary object as raw map
	 * @return a ConceptDictionary object, that behaves like a facade for the given
	 *         map
	 */
	public static ConceptDictionary createAsFacade(Map<String, Object> map) {
		if (map == null) {
			return null;
		}

		ConceptDictionary ret = new ConceptDictionary();
		ret.setMap(map);
		return ret;
	}

	@Override
	public String getIdShort() {
		return Referable.createAsFacade(this).getIdShort();
	}

	@Override
	public String getCategory() {
		return Referable.createAsFacade(this).getCategory();
	}

	@Override
	public LangStrings getDescription() {
		return Referable.createAsFacade(this).getDescription();
	}

	@Override
	public IReference getParent() {
		return Referable.createAsFacade(this).getParent();
	}

	public void setIdShort(String idShort) {
		Referable.createAsFacade(this).setIdShort(idShort);
	}

	public void setCategory(String category) {
		Referable.createAsFacade(this).setCategory(category);
	}

	public void setDescription(LangStrings description) {
		Referable.createAsFacade(this).setDescription(description);
	}

	public void setParent(IReference obj) {
		Referable.createAsFacade(this).setParent(obj);
	}

	@Override
	public Collection<IReference> getConceptDescriptionReferences() {
		return ReferenceHelper.transform(get(ConceptDictionary.CONCEPTDESCRIPTION));
	}

	/**
	 * Sets
	 * 
	 * @param ref
	 */
	public void setConceptDescriptionReferences(Collection<IReference> ref) {
		put(ConceptDictionary.CONCEPTDESCRIPTION, ref);
	}

	/**
	 * Sets the concept descriptions for this concept dictionary. The method sets local references to the added
	 * concept descriptions, too.
	 * 
	 * @param descriptions All the concept descriptions the concept dictionary shall have
	 */
	public void setConceptDescriptions(Collection<IConceptDescription> descriptions) {
		put(CONCEPTDESCRIPTIONS, descriptions);
		// Also add the references to these concept descriptions
		Collection<IReference> refs = new ArrayList<>();
		for ( IConceptDescription desc : descriptions ) {
			refs.add(createConceptDescriptionRef(desc));
		}
		setConceptDescriptionReferences(refs);
	}

	/**
	 * Adds a new concept description together with a local reference to it.
	 * 
	 * @param description The new concept description
	 */
	@SuppressWarnings("unchecked")
	public void addConceptDescription(IConceptDescription description) {
		((Collection<IConceptDescription>) get(CONCEPTDESCRIPTIONS)).add(description);
		getConceptDescriptionReferences().add(createConceptDescriptionRef(description));
	}

	private IReference createConceptDescriptionRef(IConceptDescription description) {
		IIdentifier id = description.getIdentification();
		return new Reference(new Key(KeyElements.CONCEPTDESCRIPTION, true, id.getId(), id.getIdType()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<IConceptDescription> getConceptDescriptions() {
		return ((Collection<IConceptDescription>) get(CONCEPTDESCRIPTIONS));
	}
}
