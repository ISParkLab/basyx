package org.eclipse.basyx.submodel.metamodel.connected.submodelelement.dataelement;

import org.eclipse.basyx.submodel.metamodel.api.reference.enums.KeyElements;
import org.eclipse.basyx.submodel.metamodel.api.submodelelement.dataelement.IBlob;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.Blob;
import org.eclipse.basyx.submodel.metamodel.map.submodelelement.dataelement.property.Property;
import org.eclipse.basyx.vab.modelprovider.VABElementProxy;

/**
 * "Connected" implementation of IBlob
 * @author rajashek
 *
 */
public class ConnectedBlob extends ConnectedDataElement implements IBlob {
	
	public ConnectedBlob(VABElementProxy proxy) {
		super(proxy);		
	}

	@Override
	public byte[] getValue() {
		
		// FIXME: This is a hack, fix this when API is clear
		return ((String) getProxy().getModelPropertyValue(Property.VALUE)).getBytes();
	}

	@Override
	public String getMimeType() {
		return (String)	getElem().get(Blob.MIMETYPE);
	}
	
	@Override
	protected KeyElements getKeyElement() {
		return KeyElements.BLOB;
	}
}

