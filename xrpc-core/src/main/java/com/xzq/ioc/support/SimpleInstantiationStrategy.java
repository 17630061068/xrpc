package com.xzq.ioc.support;

import com.xzq.ioc.BeansException;
import com.xzq.ioc.bean.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * jdk 实例化策略类
 *     反射创建实例
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:55
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(BeanDefinition beanDefinition, Constructor ctor, Object[] args, String beanName) throws BeansException {

        Class beanClass = beanDefinition.getBeanClass();

        try {
            if (ctor != null) {
                //使用构造函数创建bean
                return ctor.newInstance(args);
            } else {
                return beanClass.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
