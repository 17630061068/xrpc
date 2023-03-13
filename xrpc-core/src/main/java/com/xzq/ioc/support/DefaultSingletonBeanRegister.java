package com.xzq.ioc.support;

import com.xzq.ioc.factory.SingletonBeanRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:39
 */
public class DefaultSingletonBeanRegister implements SingletonBeanRegister {

    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void addSingleton(String beanName, Object singleton) {
        singletonObjects.put(beanName, singleton);
    }

}
