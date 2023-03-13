package com.xzq.client.enums;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 15:52
 */
public enum ConnectionEnums {

    UN_CONNECTION(0,"未连接"),

    CONNECTION(1,"已连接")
    ;

    private Integer status;

    private String desc;

    ConnectionEnums(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}
