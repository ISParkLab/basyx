package org.eclipse.basyx.submodel.metamodel.connected;

import java.util.Map;

import org.eclipse.basyx.submodel.metamodel.api.IElement;
import org.eclipse.basyx.submodel.metamodel.map.qualifier.Referable;
import org.eclipse.basyx.vab.model.VABModelMap;
import org.eclipse.basyx.vab.modelprovider.VABElementProxy;

/**
 * Conntected Element superclass; Extends HashMap for local caching used for c# proxy
 * 
 * @author pschorn
 *
 */
public class ConnectedElement implements IElement {

	private VABElementProxy proxy;
	private VABModelMap<Object> cached;

	public VABElementProxy getProxy() {
		return proxy;
	}

	public ConnectedElement(VABElementProxy proxy) {
		super();
		this.proxy = proxy;
		cached = getElemLive();
	}


	/**
	 * Returns a live variant of the map. Only use this if access to dynamic data is
	 * intended. Otherwise use {@link #getElem()}
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected VABModelMap<Object> getElemLive() {
		VABModelMap<Object> map = new VABModelMap<Object>((Map<String, Object>) getProxy().getModelPropertyValue(""));
		return map;
	}

	/**
	 * Returns the cached variant of the underlying element. <br>
	 * Only use this method if you are accessing static data (e.g. meta data) of the
	 * element. Otherwise use {@link #getElemLive()}
	 * 
	 * @return
	 */
	protected VABModelMap<Object> getElem() {
		return cached;
	}

	protected void throwNotSupportedException() {
		throw new RuntimeException("Not supported on remote object");
	}

	@Override
	public String getIdShort() {
		return (String) getElem().getPath(Referable.IDSHORT);
	}
}
