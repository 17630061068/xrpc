package com.xzq.client.factory;

import com.xzq.client.XrpcClient;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 11:46
 */
public abstract class XrpcClientFactory {

    private ReentrantLock instanceLock = new ReentrantLock();

    private Map<String, XrpcClient> xrpcClientMap = new ConcurrentHashMap<>();

    private XrpcProtocol xrpcProtocol;

    public XrpcClientFactory() {
    }

    public XrpcClientFactory(XrpcProtocol xrpcProtocol) {
        this.xrpcProtocol = xrpcProtocol;
    }


    public  XrpcClient getInstance(String host, int port) {
        XrpcClient xrpcClient = xrpcClientMap.get(getKey(host, port));

        if (xrpcClient == null) {

            instanceLock.lock();
            try {
                xrpcClient = new XrpcClient(xrpcProtocol, new NioEventLoopGroup());
            }finally {
                instanceLock.unlock();
            }
        }

        return xrpcClient;
    }

    protected abstract String getKey(String host, int port);

}
