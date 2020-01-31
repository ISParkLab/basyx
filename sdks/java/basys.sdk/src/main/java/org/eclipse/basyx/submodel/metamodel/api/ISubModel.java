package org.eclipse.basyx.submodel.metamodel.api;

import org.eclipse.basyx.submodel.metamodel.api.qualifier.IHasDataSpecification;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.IHasSemantics;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.IIdentifiable;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.haskind.IHasKind;
import org.eclipse.basyx.submodel.metamodel.api.qualifier.qualifiable.IQualifiable;

/**
 * A submodel defines a specific aspect of the asset represented by the
 * AAS.<br/>
 * <br />
 * A submodel is used to structure the digital representation and technical
 * functionality of an Administration Shell into distinguishable parts. Each
 * submodel refers to a well-defined domain or subject matter. Submodels can
 * become standardized and thus become submodels types. Submodels can have
 * different life-cycles.
 * 
 * @author kuhn, schnicke
 *
 */
public interface ISubModel extends IElement, IHasSemantics, IIdentifiable, IQualifiable, IHasDataSpecification, IHasKind, IElementContainer {
}
