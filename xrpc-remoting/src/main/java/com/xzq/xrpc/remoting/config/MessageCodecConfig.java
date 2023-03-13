package com.xzq.xrpc.remoting.config;

import com.xzq.xrpc.remoting.protocol.XrpcProtocol;

/**
 *
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 16:48
 */
public class MessageCodecConfig {

    public MessageCodecConfig(XrpcProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * xrpc协议
     */
    private XrpcProtocol protocol;


    public XrpcProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(XrpcProtocol protocol) {
        this.protocol = protocol;
    }
}
