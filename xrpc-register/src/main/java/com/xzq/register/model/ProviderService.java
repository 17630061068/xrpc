package com.xzq.register.model;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 18:53
 */
public class ProviderService {
    private String serviceName;
    private String host;
    private int port;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
