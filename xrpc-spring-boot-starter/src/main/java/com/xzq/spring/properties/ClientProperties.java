package com.xzq.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/14 22:55
 */
@ConfigurationProperties(prefix = "xrpc.client")
@Data
public class ClientProperties {

    private Long keepAliveTime;
}
