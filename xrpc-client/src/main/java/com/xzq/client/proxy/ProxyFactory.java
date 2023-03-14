package com.xzq.client.proxy;

import com.xzq.client.XrpcClient;
import com.xzq.client.XrpcClientConfig;
import com.xzq.client.factory.XrpcClientFactory;
import com.xzq.client.proxy.cglib.XrpcClientCglibProxy;
import com.xzq.client.proxy.jdk.XrpcClientJdkProxy;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import io.netty.bootstrap.Bootstrap;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @desc 代理工厂
 * 优化点:
 * 1.client客户端应该被ioc管理起来
 * 2.client客户端代理对象应该实行懒加载，即调用时创建
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 18:23
 */
public class ProxyFactory extends XrpcClientFactory {

    private static final String PROXY_TYPE_JDK = "jdk";
    private static final String PROXY_TYPE_CGLIB = "cglib";

    public ProxyFactory() {

    }

    public ProxyFactory(XrpcProtocol xrpcProtocol, Bootstrap bootstrap, XrpcClientConfig xrpcClientConfig) {
        super(xrpcProtocol, bootstrap, xrpcClientConfig);
    }

    public <T> T getProxy(Class<T> target, String host, int port) {
        return getProxy(target, host, port, PROXY_TYPE_JDK);
    }

    public <T> T getProxy(Class<T> target, String host, int port, String type) {

        switch (type) {
            case PROXY_TYPE_JDK:
                return newProxyInstanceByJDK(target, host, port);
            case PROXY_TYPE_CGLIB:
                return newProxyInstanceByCglib(target, host, port);
        }

        throw new RuntimeException("未匹配到合适的代理类型");
    }

    private <T> T newProxyInstanceByCglib(Class<T> target, String host, int port) {

        XrpcClient instance = getInstance(host, port);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target);
        enhancer.setCallback(new XrpcClientCglibProxy(target, instance,host,port));

        return (T) enhancer.create();
    }

    private <T> T newProxyInstanceByJDK(Class<T> target, String host, int port) {

        XrpcClient instance = getInstance(host, port);

        ClassLoader classLoader = target.getClassLoader();

        Class[] interfaces = {target};

        return (T) Proxy.newProxyInstance(classLoader, interfaces, new XrpcClientJdkProxy(target, instance, host, port));
    }


    @Override
    protected String getKey(String host, int port) {
        return host + ":" + port;
    }

}
