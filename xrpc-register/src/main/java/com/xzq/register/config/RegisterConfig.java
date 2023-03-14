package com.xzq.register.config;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 13:59
 */

public class RegisterConfig {

    private String host;

    private int port;

    private String auth;

    public RegisterConfig() {
    }

    public RegisterConfig(String host, int port, String auth) {
        this.host = host;
        this.port = port;
        this.auth = auth;
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

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
