package com.xzq.bootstrap.test;

import com.xzq.ioc.annotation.XrpcService;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 16:06
 */
@XrpcService
public class ProviderTest implements UserClient {

    @Override
    public String test(String name) {
        return "rpc进行了调用" + name;
    }
}
