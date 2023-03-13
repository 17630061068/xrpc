package com.xzq.ioc.support;

import com.xzq.ioc.bean.BeanDefinition;

/**
 * bean定义注册接口
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:22
 */
public interface BeanDefinitionRegister {


    /**
     * 向注册表中注册beanDefinition
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 向注册表中注册beanDefinition
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(BeanDefinition beanDefinition);

}
