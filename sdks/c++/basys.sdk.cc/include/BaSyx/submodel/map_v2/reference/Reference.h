#ifndef BASYX_SUBMODEL_MAP_V2_REFERENCE_REFERENCE_H
#define BASYX_SUBMODEL_MAP_V2_REFERENCE_REFERENCE_H

#include <BaSyx/submodel/api_v2/reference/IReference.h>
#include <BaSyx/submodel/simple/reference/Key.h>

#include <BaSyx/vab/ElementMap.h>

namespace basyx {
namespace submodel {
namespace map {

class Reference : 
	public api::IReference,
	public virtual vab::ElementMap
{
public:
	struct Path	{
		static constexpr char Keys[] = "keys";
	};
public:
	Reference();
public:
	using vab::ElementMap::ElementMap;

	Reference(const api::IReference & other);
	Reference(const Reference & other) = default;
	Reference(Reference && other) noexcept = default;
  Reference(basyx::object & object);

	Reference & operator=(const api::IReference & other);
	Reference & operator=(const Reference & other) = default;
	Reference & operator=(Reference && other) noexcept = default;

	Reference(const simple::Key & key);
	Reference(const std::vector<simple::Key> & keys);

	virtual ~Reference() = default;
public:
	std::vector<simple::Key> getKeys() const override;
	void addKey(const simple::Key & key) override;

	virtual bool empty() const override;
public:
//	static Reference FromIdentifiable(const std::string & keyElementType, bool local, const IIdentifiable & identifiable);

private:
  static simple::Key keyMap_to_key(basyx::object &keyMap);
  static std::vector<simple::Key> keyMapList_to_keyList(basyx::object::object_list_t &keyMapList);
};

}
}
}

#endif /* BASYX_SUBMODEL_MAP_V2_REFERENCE_REFERENCE_H */
