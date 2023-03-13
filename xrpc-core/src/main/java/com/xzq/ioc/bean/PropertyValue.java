package com.xzq.ioc.bean;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:14
 */
public class PropertyValue {
    private String name;
    private Object value;

    public PropertyValue() {

    }

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
