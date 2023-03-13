package com.xzq.ioc.factory;

/**
 * @desc bean注册器
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 19:08
 */
public interface BeanRegister {

    void registerBean(String beanName, Object bean);

}
