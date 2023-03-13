package com.xzq.ioc.bean;

import java.util.List;

/**
 * bean 定义
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:12
 */
public class BeanDefinition {

    private Class beanClass;
    private String beanName;

    private List<PropertyValue> propertyValueList;

    private String initMethodName;
    private String destroyMethodName;

    private Boolean singleton = Boolean.TRUE;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public void setPropertyValueList(List<PropertyValue> propertyValueList) {
        this.propertyValueList = propertyValueList;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Boolean isSingleton() {
        return singleton;
    }

}
