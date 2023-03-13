package com.xzq.bootstrap;

import com.xzq.register.config.RegisterConfig;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class XrpcServerBootStrapTest {

    public static void main(String[] args) {
        RegisterConfig registerConfig = new RegisterConfig();
        registerConfig.setHost("175.24.165.137");
        registerConfig.setPort(16379);
        registerConfig.setAuth("xzq");

        XrpcServerBootStrap boot = new XrpcServerBootStrap(registerConfig, "com.xzq.bootstrap.test");

        boot.setEnableServer(Boolean.TRUE);

        boot.start();

    }


}