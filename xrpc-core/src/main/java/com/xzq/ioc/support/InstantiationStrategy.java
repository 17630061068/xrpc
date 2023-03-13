package com.xzq.ioc.support;


import com.xzq.ioc.BeansException;
import com.xzq.ioc.bean.BeanDefinition;

import java.lang.reflect.Constructor;

/**
 * 实例化bean策略类
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:52
 */
public interface InstantiationStrategy {


    Object instantiate(BeanDefinition beanDefinition, Constructor ctor, Object[] args, String beanName) throws BeansException;

}
