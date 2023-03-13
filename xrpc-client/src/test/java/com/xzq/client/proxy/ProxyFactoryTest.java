package com.xzq.client.proxy;

import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyFactoryTest {


    @Test
    public void test_xrpcClient() {

        ProxyFactory proxyFactory = new ProxyFactory(XrpcProtocol.DEFAULT_PROTOCOL());

        UserClient userClient = proxyFactory.getProxy(UserClient.class, "127.0.0.1", 9999);

        System.out.println(userClient);

        userClient.test("123456");


    }

}