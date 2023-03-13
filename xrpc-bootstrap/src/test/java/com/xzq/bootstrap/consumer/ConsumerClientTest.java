package com.xzq.bootstrap.consumer;

import com.xzq.bootstrap.test.UserClient;
import com.xzq.ioc.annotation.Autowired;
import com.xzq.ioc.annotation.Service;
import com.xzq.ioc.annotation.XrpcAutowired;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 16:34
 */
@Service
public class ConsumerClientTest {

    @XrpcAutowired
    private UserClient userClient;

    public String test(String name) {
        return userClient.test(name);
    }
}
