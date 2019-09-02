package org.eclipse.basyx.aas.backend.connected.aas.submodelelement.property;

import org.eclipse.basyx.aas.api.metamodel.aas.reference.IReference;
import org.eclipse.basyx.aas.api.metamodel.aas.submodelelement.property.IReferenceElement;
import org.eclipse.basyx.aas.backend.connected.aas.submodelelement.ConnectedDataElement;
import org.eclipse.basyx.aas.metamodel.hashmap.aas.submodelelement.property.Property;
import org.eclipse.basyx.vab.core.proxy.VABElementProxy;
/**
 * "Connected" implementation of IReferenceElement
 * @author rajashek
 *
 */
public class ConnectedReferenceElement extends ConnectedDataElement implements IReferenceElement {
	public ConnectedReferenceElement(VABElementProxy proxy) {
		super(proxy);		
	}
	

	@Override
	public void setValue(IReference ref) {
		getProxy().setModelPropertyValue(Property.VALUE, ref);
		
	}

	@Override
	public IReference getValue() {
		return (IReference)getProxy().getModelPropertyValue(Property.VALUE);
	}

}
