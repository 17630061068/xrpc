package com.xzq.ioc.factory;

/**
 * 单例bean注册接口
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:28
 */
public interface SingletonBeanRegister {

    /**
     * 获取单例bean
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName);

    /**
     * 添加到单例池
     * @param beanName
     * @param singleton
     */
    void addSingleton(String beanName, Object singleton);
}
