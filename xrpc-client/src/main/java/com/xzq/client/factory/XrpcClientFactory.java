package com.xzq.client.factory;

import com.xzq.client.XrpcClient;
import com.xzq.util.CollectionUtil;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import io.netty.bootstrap.Bootstrap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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

    private Bootstrap bootstrap;

    private  ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
            1
    );

    public XrpcClientFactory() {

    }

    public XrpcClientFactory(XrpcProtocol xrpcProtocol, Bootstrap bootstrap) {
        this.xrpcProtocol = xrpcProtocol;
        this.bootstrap = bootstrap;
        //空闲检测并下线
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            this.closeIfKeepAlive();
        }, 0,5, TimeUnit.SECONDS);
    }


    public  XrpcClient getInstance(String host, int port) {
        XrpcClient xrpcClient = xrpcClientMap.get(getKey(host, port));

        if (xrpcClient == null) {

            instanceLock.lock();
            try {

                if (xrpcClient == null) {

                    xrpcClient = new XrpcClient(xrpcProtocol, bootstrap);
                }

            }finally {
                xrpcClientMap.put(getKey(host, port), xrpcClient);
                instanceLock.unlock();
            }
        }

        return xrpcClient;
    }

    protected abstract String getKey(String host, int port);


    /**
     * 空闲检测
     *     如果空闲执行下线
     */
    protected void closeIfKeepAlive() {
        if (CollectionUtil.isEmpty(xrpcClientMap)) {
            return;
        }

        xrpcClientMap.values().stream().forEach(XrpcClient::closeFutureIfKeepAlive);

    }

}
