/*
* BaSyxNativeConnector.h
*
*  Created on: 14.08.2018
*      Author: schnicke
*/

#ifndef VAB_VAB_BACKEND_CONNECTOR_NATIVE_BASYXCONNECTOR_H
#define VAB_VAB_BACKEND_CONNECTOR_NATIVE_BASYXCONNECTOR_H

#include <BaSyx/vab/backend/connector/IBaSyxConnector.h>
#include <BaSyx/vab/backend/connector/native/frame/BaSyxNativeFrameBuilder.h>

#include <BaSyx/abstraction/Net.h>

#include <memory>

namespace basyx {
namespace vab {
namespace connector {
namespace native {

    class NativeConnector : public IBaSyxConnector {
    public:
        static constexpr std::size_t default_buffer_length = 4096;

    public:
        NativeConnector(std::string const& address, int port);

        virtual ~NativeConnector();

    public:
        virtual ::basyx::object basysGet(std::string const& path) override;

        virtual nlohmann::json basysGetRaw(std::string const& path) override;

        virtual void basysSet(std::string const& path, const ::basyx::object& newValue) override;

        virtual void basysCreate(std::string const& servicePath, const ::basyx::object& val)
            override;

        virtual ::basyx::object basysInvoke(std::string const& servicePath, const ::basyx::object& param)
            override;

        virtual void basysDelete(std::string const& servicePath) override;

        virtual void basysDelete(std::string const& servicePath, const ::basyx::object& obj) override;

    private:
        basyx::net::tcp::Socket socket;
        frame::BaSyxNativeFrameBuilder builder;
        std::array<char, default_buffer_length> buffer;

        void sendData(char* data, size_t size);

        size_t receiveData(char* data);
        basyx::object decode(char* buffer);
        basyx::log log;
    };

}
}
}
};

#endif /* VAB_VAB_BACKEND_CONNECTOR_NATIVE_BASYXCONNECTOR_H */