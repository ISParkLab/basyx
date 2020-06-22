package org.eclipse.basyx.submodel.metamodel.api.submodelelement.dataelement;

import org.eclipse.basyx.submodel.metamodel.api.IElement;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;
import org.eclipse.basyx.vab.exception.provider.ProviderException;

/**
 * Interface for IElement properties
 * 
 * @author kuhn
 *
 */
public interface IProperty extends IElement, IDataElement {
	/**
	 * Get property value
	 * 
	 * @return Property value
	 * @throws Exception
	 */
	public Object get() throws Exception;

	/**
	 * Set property value
	 * 
	 * @throws ProviderException
	 */
	public void set(Object newValue) throws ProviderException;

	/**
	 * Gets the data type of the value
	 * 
	 * @return
	 */
	public String getValueType();

	/**
	 * Gets the reference to the global unique id of a coded value.
	 * 
	 * @return
	 */
	public IReference getValueId();
}
