package com.xzq.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 23:32
 */
@ConfigurationProperties(prefix = "xrpc")
@Data
public class XrpcProperties {

    private ServerProperties server;

    private RegisterProperties register;

    private ClientProperties client;


}
