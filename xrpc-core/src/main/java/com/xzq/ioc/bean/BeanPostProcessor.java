package com.xzq.ioc.bean;

import com.xzq.ioc.BeansException;

/**
 * Bean扩展点接口
 *
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 12:01
 */
public interface BeanPostProcessor {
    /**
     * 在bean对象执行初始化方法之前，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    };

    /**
     * 在bean对象执行初始化方法之后，执行此方法
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException{
        return bean;
    };
}
