package com.xzq.bootstrap;

import com.xzq.bootstrap.consumer.ConsumerClientTest;
import com.xzq.ioc.context.ClassPathApplicationContext;
import com.xzq.register.config.RegisterConfig;

import java.net.InetSocketAddress;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 16:33
 */
public class ConsumerTest {
    public static void main(String[] args) {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setHost("175.24.165.137");
        registerConfig.setPort(16379);
        registerConfig.setAuth("xzq");

        XrpcServerBootStrap boot = new XrpcServerBootStrap(registerConfig, "com.xzq.bootstrap.consumer");

        boot.setEnableServer(Boolean.FALSE);

        boot.start();

        ClassPathApplicationContext applicationContext = boot.getApplicationContext();

        ConsumerClientTest consumerTest = applicationContext.getBeansOfType(ConsumerClientTest.class).get(0);

        String result = consumerTest.test("熊志强");

        System.out.println(result);


    }
}
