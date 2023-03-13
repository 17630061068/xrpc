package com.xzq.ioc.support;

import com.xzq.ioc.BeansException;
import com.xzq.ioc.bean.BeanDefinition;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

/**
 * cglib 实例化策略类
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 11:00
 */
public class CglibSubclassingInstantiationStrategy implements InstantiationStrategy{


    @Override
    public Object instantiate(BeanDefinition beanDefinition, Constructor ctor, Object[] args, String beanName) throws BeansException {
        Enhancer enhancer = new Enhancer();

        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });

        if (ctor == null) {
            return enhancer.create();
        }

        return enhancer.create(ctor.getParameterTypes(), args);
    }
}
