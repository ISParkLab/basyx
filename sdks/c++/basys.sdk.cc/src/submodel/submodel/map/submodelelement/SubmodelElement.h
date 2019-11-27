/*
 * SubmodelElement.h
 *
 *      Author: wendel
 */

#ifndef IMPL_METAMODEL_MAP_AAS_SUBMODELELEMENT_SUBMODELELEMENT_H_
#define IMPL_METAMODEL_MAP_AAS_SUBMODELELEMENT_SUBMODELELEMENT_H_

#include "submodel/api/submodelelement/ISubmodelElement.h"

#include "submodel/map/qualifier/HasKind.h"
#include "submodel/map/qualifier/HasDataSpecification.h"
#include "submodel/map/qualifier/HasSemantics.h"
#include "submodel/map/qualifier/Referable.h"
#include "submodel/map/qualifier/qualifiable/Qualifiable.h"
#include "submodel/map/modeltype/ModelType.h"

#include "vab/ElementMap.h"

namespace basyx {
namespace submodel {

class SubmodelElement : 
	public virtual ModelType,
	public HasDataSpecification,
	public HasKind,
	public HasSemantics,
	public Qualifiable,
	public Referable,
	public ISubmodelElement
{
public:
	~SubmodelElement() = default;

	// constructors
	SubmodelElement();

	/**
	* Constructs an SubmodelElement object from a map given that the map contains all required elements.
	* 
	* @param submodelElementMap the map representig the submodel.
	*/
	SubmodelElement(basyx::object object);
};

}
}

#endif