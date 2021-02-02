package org.eclipse.basyx.submodel.metamodel.api.submodelelement.relationship;

import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.ISubmodelElement;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.relationship.RelationshipElementValue;

/**
 * A relationship element is used to define a relationship between two referable
 * elements.
 * 
 * @author rajashek, schnicke
 *
 */
public interface IRelationshipElement extends ISubmodelElement {
	/**
	 * Gets the first element in the relationship taking the role of the subject.
	 * 
	 * @return
	 */
	IReference getFirst();

	/**
	 * Gets the second element in the relationship taking the role of the object.
	 * 
	 * @return
	 */
	IReference getSecond();
	
	@Override
	RelationshipElementValue getValue();

	/**
	 * Sets the relationship of the RelationshipElement submodel element
	 * 
	 * @param value
	 */
	void setValue(RelationshipElementValue value);
}
