package com.xzq.spring.properties;

import lombok.Data;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 23:38
 */
@Data
public class ServerProperties {

    /**
     * 是否启动服务
     */
    private Boolean enable = Boolean.FALSE;

    /**
     * 指定服务端口
     */
    private Integer port;
}