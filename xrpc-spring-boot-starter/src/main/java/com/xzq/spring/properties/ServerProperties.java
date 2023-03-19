package com.xzq.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 23:38
 */
@Data
@ConfigurationProperties(prefix = "xrpc.server")
public class ServerProperties {

    /**
     * 是否启动服务
     */
    private Boolean enable = Boolean.FALSE;

    /**
     * 是否使用公网ip
     */
    private Boolean enablePublicIp = Boolean.FALSE;

    /**
     * 指定服务端口
     */
    private Integer port;

}
