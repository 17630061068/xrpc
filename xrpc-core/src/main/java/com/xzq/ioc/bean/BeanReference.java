package com.xzq.ioc.bean;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:07
 */
public class BeanReference {

    private String beanName;

    public BeanReference() {
    }

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
