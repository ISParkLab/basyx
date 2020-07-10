/*
 * test_BaSyxNativeProvider.cpp
 *
 *  Created on: 07.08.2018
 *      Author: schnicke
 */

#include <string>
#include <memory>

#include <gtest/gtest.h>

#include <BaSyx/shared/types.h>

#include <BaSyx/vab/provider/native/frame/BaSyxNativeFrameProcessor.h>

#include <BaSyx/util/tools/CoderTools.h>
#include <BaSyx/util/tools/StringTools.h>

#include "support/MockupModelProvider.h"

using namespace basyx;
using Frame = basyx::vab::connector::native::Frame;

TEST(BaSyxNativeFrameProcessor, getTest) {
	auto mockup = util::make_unique<MockupModelProvider>();
	vab::provider::native::frame::BaSyxNativeFrameProcessor provider(mockup.get());

	auto inputFrame = Frame::Builder::Get("test");
	auto answerFrame = provider.processInputFrame(inputFrame);

	ASSERT_EQ(mockup->path, "test");
	ASSERT_EQ(mockup->called, MockupModelProvider::CalledFunction::GET);
	ASSERT_EQ(answerFrame.getFlag(), 0);
	
	basyx::object deserialzed = basyx::serialization::json::deserialize(answerFrame.getFirstValue());

	ASSERT_TRUE(deserialzed.InstanceOf<basyx::object::object_map_t>());
	auto & entityWrapper = deserialzed.Get<basyx::object::object_map_t&>();

	ASSERT_TRUE(entityWrapper.count("entity") == 1);
	auto val = entityWrapper["entity"];

	ASSERT_TRUE(val.InstanceOf<int>());
	ASSERT_EQ(val.Get<int>(), 2);
}

TEST(BaSyxNativeFrameProcessor, setTest) {
	auto mockup = util::make_unique<MockupModelProvider>();
	vab::provider::native::frame::BaSyxNativeFrameProcessor provider(mockup.get());

	const std::string path = "TestPath/aas";

	auto inputFrame = Frame::Builder::Set(path, 10);
	auto answerFrame = provider.processInputFrame(inputFrame);

	ASSERT_EQ(answerFrame.getFlag(), 0);

	ASSERT_EQ(mockup->called, MockupModelProvider::CalledFunction::SET);

	ASSERT_EQ(mockup->path, path);

	ASSERT_TRUE(mockup->val.InstanceOf<int>());
	ASSERT_EQ(mockup->val.Get<int>(), 10);
}

TEST(BaSyxNativeFrameProcessor, createTest) {
	auto mockup = util::make_unique<MockupModelProvider>();
	vab::provider::native::frame::BaSyxNativeFrameProcessor provider(mockup.get());

	std::string path = "TestPath";

	auto inputFrame = Frame::Builder::Create(path, 10);
	auto answerFrame = provider.processInputFrame(inputFrame);

	ASSERT_EQ(answerFrame.getFlag(), 0);

	ASSERT_EQ(mockup->called, MockupModelProvider::CalledFunction::CREATE);

	ASSERT_EQ(mockup->path, path);

	ASSERT_TRUE(mockup->val.InstanceOf<int>());
	ASSERT_EQ(mockup->val.Get<int>(), 10);
}

TEST(BaSyxNativeFrameProcessor, deleteComplexTest) {
	auto mockup = util::make_unique<MockupModelProvider>();
	vab::provider::native::frame::BaSyxNativeFrameProcessor provider(mockup.get());

	std::string path = "TestPath/aas/properties/C1";

	auto inputFrame = Frame::Builder::Delete(path, 10);
	auto answerFrame = provider.processInputFrame(inputFrame);

	ASSERT_EQ(answerFrame.getFlag(), 0);

	ASSERT_EQ(mockup->called,
			MockupModelProvider::CalledFunction::DELETE_COMPLEX);

	ASSERT_EQ(mockup->path, path);

	ASSERT_TRUE(mockup->val.InstanceOf<int>());
	ASSERT_EQ(mockup->val.Get<int>(), 10);
}

TEST(BaSyxNativeFrameProcessor, deleteSimpleTest) {
	auto mockup = util::make_unique<MockupModelProvider>();
	vab::provider::native::frame::BaSyxNativeFrameProcessor provider(mockup.get());

	std::string path = "TestPath/aas/properties/P1";

	auto inputFrame = Frame::Builder::Delete(path);
	auto answerFrame = provider.processInputFrame(inputFrame);

	ASSERT_EQ(answerFrame.getFlag(), 0);

	ASSERT_EQ(mockup->called, MockupModelProvider::CalledFunction::DELETE_SIMPLE);

	ASSERT_EQ(mockup->path, path);
}

TEST(BaSyxNativeFrameProcessor, invokeTest) {
	auto mockup = util::make_unique<MockupModelProvider>();
	vab::provider::native::frame::BaSyxNativeFrameProcessor provider{ mockup.get() };

	std::string path = "TestPath";

	auto inputFrame = Frame::Builder::Invoke(path, 10);
	auto answerFrame = provider.processInputFrame(inputFrame);

	ASSERT_EQ(mockup->called, MockupModelProvider::CalledFunction::INVOKE);
	ASSERT_EQ(mockup->path, path);
	ASSERT_TRUE(mockup->val.InstanceOf<int>());
	ASSERT_EQ(mockup->val.Get<int>(), 10);

	ASSERT_EQ(answerFrame.getFlag(), 0);

	// Deserialize return value of operation
	auto val = basyx::serialization::json::deserialize(nlohmann::json::parse(answerFrame.getFirstValue())["entity"]);
	ASSERT_TRUE(val.InstanceOf<int>());
	ASSERT_EQ(val.Get<int>(),3);
}