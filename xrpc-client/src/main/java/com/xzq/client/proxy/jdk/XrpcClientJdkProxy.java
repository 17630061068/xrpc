package com.xzq.client.proxy.jdk;

import com.xzq.client.XrpcClient;
import com.xzq.client.XrpcMessageHandler;
import com.xzq.xrpc.remoting.message.XrpcRequestMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * xrpc 客户端 jdk代理类
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 18:24
 */
public class XrpcClientJdkProxy implements InvocationHandler {
    private Class target;
    private XrpcClient client;
    private String host;
    private Integer port;


    public XrpcClientJdkProxy(Class target, XrpcClient client, String host, Integer port) {
        this.target = target;
        this.client = client;
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        client.connect(new InetSocketAddress(host, port));

        XrpcRequestMessage request = XrpcRequestMessage.builder()
                .interfaceName(target.getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameterValues(args)
                .returnType(method.getReturnType())
                .build();

        return client.invoke(request);
    }

}
