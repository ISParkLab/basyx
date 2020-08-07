#include <gtest/gtest.h>
#include <BaSyx/submodel/simple/qualifier/AdministrativeInformation.h>
#include <BaSyx/submodel/map_v2/qualifier/AdministrativeInformation.h>

using namespace basyx::submodel;

// Implementations to run tests for
using ImplTypes = ::testing::Types<
	simple::AdministrativeInformation,
	map::AdministrativeInformation
>;

template<class Impl>
class AdministrativeInformationTest :public ::testing::Test {
protected:
	using impl_t = Impl;
protected:
	std::unique_ptr<api::IAdministrativeInformation> administrativeInformation;
  std::unique_ptr<api::IAdministrativeInformation> administrativeInformationMembersSet;
protected:
	void SetUp() override
	{
		this->administrativeInformation = util::make_unique<Impl>();
    this->administrativeInformationMembersSet = util::make_unique<Impl>("TestVersion", "TestRevision");
	}

	void TearDown() override
	{
	}
};

TYPED_TEST_CASE(AdministrativeInformationTest, ImplTypes);

TYPED_TEST(AdministrativeInformationTest, TestConstructor)
{
  ASSERT_EQ(this->administrativeInformation->getRevision(), nullptr);
  ASSERT_EQ(this->administrativeInformation->getVersion(), nullptr);
}

TYPED_TEST(AdministrativeInformationTest, TestStringConstructor)
{
  ASSERT_EQ(*this->administrativeInformationMembersSet->getVersion(), "TestVersion");
  ASSERT_EQ(*this->administrativeInformationMembersSet->getRevision(), "TestRevision");
}

TYPED_TEST(AdministrativeInformationTest, TestSetVersion)
{
  ASSERT_EQ(this->administrativeInformation->getVersion(), nullptr);

  this->administrativeInformation->setVersion("TestVersion");

  ASSERT_EQ(*this->administrativeInformation->getVersion(), "TestVersion");
}

TYPED_TEST(AdministrativeInformationTest, TestSetRevision)
{
  ASSERT_EQ(this->administrativeInformation->getRevision(), nullptr);

  this->administrativeInformation->setRevision("TestRevision");

  ASSERT_EQ(*this->administrativeInformation->getRevision(), "TestRevision");
}

TYPED_TEST(AdministrativeInformationTest, TestHasVersion)
{
  ASSERT_FALSE(this->administrativeInformation->hasVersion());

  this->administrativeInformation->setVersion("TestVersion");

  ASSERT_TRUE(this->administrativeInformation->hasVersion());
}

TYPED_TEST(AdministrativeInformationTest, TestHasRevision)
{
  ASSERT_FALSE(this->administrativeInformation->hasRevision());

  this->administrativeInformation->setRevision("TestRevision");

  ASSERT_TRUE(this->administrativeInformation->hasRevision());
}

