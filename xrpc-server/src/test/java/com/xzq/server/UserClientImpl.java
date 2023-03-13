package com.xzq.server;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 16:25
 */
public class UserClientImpl implements UserClient {
    @Override
    public String test(String name) {
        System.out.println("rpc调用：" + name);
        return "rpc调用:" + name;
    }
}
