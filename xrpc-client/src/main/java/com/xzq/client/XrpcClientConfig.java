package com.xzq.client;

/**
 * @Desc 生成XrpcClient的配置类
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/14 22:52
 */
public class XrpcClientConfig {

    private Long keepAliveTime;

    public Long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
