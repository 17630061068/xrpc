package com.xzq.client.proxy.cglib;

import com.xzq.client.XrpcClient;
import com.xzq.client.XrpcMessageHandler;
import com.xzq.xrpc.remoting.message.XrpcRequestMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.DefaultPromise;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 11:26
 */
public class XrpcClientCglibProxy implements MethodInterceptor {

    private Class target;
    private XrpcClient client;

    private String host;
    private Integer port;

    public XrpcClientCglibProxy(Class target, XrpcClient client, String host, Integer port) {
        this.target = target;
        this.client = client;
        this.host = host;
        this.port = port;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

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
