package org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.submodelelement;

import org.eclipse.basyx.aas.api.metamodel.aas.reference.IReference;
import org.eclipse.basyx.aas.api.metamodel.aas.submodelelement.IReferenceElement;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.reference.Reference;
import org.eclipse.basyx.aas.impl.metamodel.hashmap.aas.submodelelement.property.SingleProperty;

/**
 * A ReferenceElement as defined in DAAS document <br/>
 * A reference element is a data element that defines a reference to another
 * element within the same or another AAS or a reference to an external object
 * or entity.
 * 
 * @author schnicke, pschorn
 *
 */
public class ReferenceElement extends DataElement implements IReferenceElement {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ReferenceElement() {
		put(SingleProperty.VALUE, null);
	}

	/**
	 * @param ref
	 *            Reference to any other referable element of the same or any other
	 *            AAS or a reference to an external object or entity
	 */
	public ReferenceElement(Reference ref) {
		put(SingleProperty.VALUE, ref);
	}

	@Override
	public void setValue(IReference ref) {
		put(SingleProperty.VALUE, ref);
		
	}

	@Override
	public IReference getValue() {
		return (IReference) get(SingleProperty.VALUE);
	}

}
