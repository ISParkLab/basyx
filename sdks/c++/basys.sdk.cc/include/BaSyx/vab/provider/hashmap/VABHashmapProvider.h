#ifndef VAB_PROVIDER_HASHMAP_VABHASHMAPPROVIDER_H
#define VAB_PROVIDER_HASHMAP_VABHASHMAPPROVIDER_H

#include <BaSyx/vab/core/IModelProvider.h>
#include <BaSyx/vab/core/util/VABPath.h>

#include <BaSyx/vab/provider/VABModelProvider.h>
#include <BaSyx/vab/provider/VABMultiElementHandler.h>

#include <BaSyx/shared/object.h>
#include <BaSyx/shared/types.h>

#include <functional>
#include <iostream>
#include <unordered_map>

namespace basyx {
namespace vab {
namespace provider {

	using HashmapModelProvider_t = basyx::vab::provider::VABModelProvider;

	class HashmapProvider : public HashmapModelProvider_t
	{
	public:
		HashmapProvider()
			: HashmapModelProvider_t(basyx::object::object_map_t{})
		{
		};

		HashmapProvider(const basyx::object::object_map_t & objectMap)
			: HashmapModelProvider_t(objectMap)
		{
		};

		HashmapProvider(basyx::object::object_map_t && objectMap)
			: HashmapModelProvider_t(std::move(objectMap))
		{
		};

		virtual ~HashmapProvider() {};
	};


}
}
}

#endif /* VAB_PROVIDER_HASHMAP_VABHASHMAPPROVIDER_H */
