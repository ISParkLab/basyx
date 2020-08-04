#include <gtest/gtest.h>

#include <BaSyx/submodel/api_v2/parts/IConceptDictionary.h>
#include <BaSyx/submodel/map_v2/parts/ConceptDictionary.h>
#include <BaSyx/submodel/simple/parts/ConceptDictionary.h>

#include <BaSyx/util/util.h>

using namespace basyx::submodel;

using ImplTypes = ::testing::Types
<
  std::tuple<map::ConceptDictionary, map::ConceptDescription>,
  std::tuple<simple::ConceptDictionary, simple::ConceptDescription>
>;

template<class Impl>
class ConceptDictionaryTest :public ::testing::Test {
protected:
  using impl_t = typename std::tuple_element<0, Impl>::type;
  using impl_desc_t = typename std::tuple_element<1, Impl>::type;

  std::unique_ptr<impl_t> conceptDictionary;
  std::unique_ptr<impl_desc_t> description;
  std::unique_ptr<impl_desc_t> description2;

protected:
	void SetUp() override
	{
    simple::Identifier id(IdentifierType::IRDI, "basyx");
    description = util::make_unique<impl_desc_t>("some description", id);

    simple::Identifier id2(IdentifierType::URI, "basyx");;
    description2 = util::make_unique<impl_desc_t>("some other description", id2);

    conceptDictionary = util::make_unique<impl_t>("test id");
	}

	void TearDown() override
	{	}
};

TYPED_TEST_CASE(ConceptDictionaryTest, ImplTypes);

TYPED_TEST(ConceptDictionaryTest, TestConstructor)
{
  auto & descriptions = this->conceptDictionary->getConceptDescriptions();

  ASSERT_EQ(descriptions.size(), 0);
}

TYPED_TEST(ConceptDictionaryTest, TestAddDescription)
{
  this->conceptDictionary->addConceptDescription(std::move(this->description));
  this->conceptDictionary->addConceptDescription(std::move(this->description2));

  auto & descriptions = this->conceptDictionary->getConceptDescriptions();

  ASSERT_EQ(descriptions.size(), 2);
  ASSERT_NE(descriptions.getElement("some description"), nullptr);
  ASSERT_NE(descriptions.getElement("some other description"), nullptr);
}