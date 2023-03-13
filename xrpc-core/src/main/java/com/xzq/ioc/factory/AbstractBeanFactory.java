package com.xzq.ioc.factory;

import com.xzq.ioc.bean.BeanDefinition;
import com.xzq.ioc.bean.BeanPostProcessor;
import com.xzq.ioc.support.DefaultSingletonBeanRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:25
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegister implements BeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    @Override
    public Object getBean(String beanName) {
        return doGetBean(beanName, null);
    }

    @Override
    public Object getBean(String beanName, Object[] args) {
        return doGetBean(beanName, args);
    }

    private Object doGetBean(String beanName, Object[] args) {
        //1. 从一级缓存中获取
        Object bean = getSingleton(beanName);
        if (bean != null) {
            return bean;
        }

        //2. 获取bean定义
        BeanDefinition beanDefinition = getBeanDefinition(beanName);

        //3. 创建实例bean并返回
        return createBean(beanName, args, beanDefinition);
    }


    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    protected abstract Object createBean(String beanName, Object[] args, BeanDefinition beanDefinition);


    protected abstract BeanDefinition getBeanDefinition(String beanName);

}
