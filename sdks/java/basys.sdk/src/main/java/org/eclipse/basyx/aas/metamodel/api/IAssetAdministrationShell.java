package org.eclipse.basyx.aas.metamodel.api;

import java.util.Collection;
import java.util.Map;

import org.eclipse.basyx.aas.metamodel.api.parts.IConceptDictionary;
import org.eclipse.basyx.aas.metamodel.api.parts.IView;
import org.eclipse.basyx.aas.metamodel.api.parts.asset.IAsset;
import org.eclipse.basyx.aas.metamodel.api.security.ISecurity;
import org.eclipse.basyx.aas.metamodel.map.descriptor.SubmodelDescriptor;
import org.eclipse.basyx.submodel.metamodel.api.IElement;
import org.eclipse.basyx.submodel.metamodel.api.ISubModel;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.IHasDataSpecification;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.IIdentifiable;
import org.eclipse.basyx.submodel.metamodel.api.reference.IReference;

/**
 * Asset Administration Shell (AAS) interface
 * 
 * @author kuhn, schnicke
 *
 */

public interface IAssetAdministrationShell extends IElement, IIdentifiable, IHasDataSpecification {
	/**
	 * Return all registered submodels of this AAS
	 * 
	 * @return
	 */
	public Map<String, ISubModel> getSubModels();

	/**
	 * Return the references to all registered submodels
	 * 
	 * @return
	 */
	public Collection<IReference> getSubmodelReferences();

	/**
	 * Add a sub model to the AAS
	 * 
	 * @param subModel
	 *            The added sub model
	 */
	public void addSubModel(SubmodelDescriptor subModel);

	/**
	 * Gets the definition of the security relevant aspects of the AAS.
	 * 
	 * @return
	 */
	public ISecurity getSecurity();

	/**
	 * Gets the reference to the AAS the AAS was derived from.
	 * 
	 * @return
	 */
	public IReference getDerivedFrom();

	/**
	 * Gets the asset the AAS is representing.
	 * 
	 * @return
	 */
	public IAsset getAsset();

	/**
	 * Gets the reference to the asset the AAS is representing.
	 * 
	 * @return
	 */
	public IReference getAssetReference();

	/**
	 * Gets the submodel descriptors
	 * 
	 * @return
	 */
	public Collection<SubmodelDescriptor> getSubModelDescriptors();

	/**
	 * Gets the views associated with the AAS. <br/>
	 * If needed stakeholder specific views can be defined on the elements of the
	 * AAS.
	 * 
	 * @return
	 */
	public Collection<IView> getViews();

	/**
	 * Gets the concept dictionaries associated with the AAS. <br/>
	 * An AAS may have one or more concept dictionaries assigned to it. The concept
	 * dictionaries typically contain only descriptions for elements that are also
	 * used within the AAS (via HasSemantics).
	 * 
	 * @return
	 */
	public Collection<IConceptDictionary> getConceptDictionary();
}
