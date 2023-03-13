package com.xzq.spring.properties;

import lombok.Data;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 23:36
 */
@Data
public class RegisterProperties {

    private Redis redis;

    private Nacos nacos;


    @Data
    public static class Redis {
        private String host;
        private Integer port;
        private String auth;
    }

    @Data
    public static class Nacos{

        private String serveraddr;

        private String metaspace;

        private String auth;
    }
}
