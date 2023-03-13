package com.xzq.ioc.factory;

import cn.hutool.core.bean.BeanUtil;
import com.xzq.ioc.bean.BeanDefinition;
import com.xzq.ioc.bean.BeanPostProcessor;
import com.xzq.ioc.bean.BeanReference;
import com.xzq.ioc.bean.PropertyValue;
import com.xzq.ioc.support.CglibSubclassingInstantiationStrategy;
import com.xzq.ioc.support.InstantiationStrategy;
import com.xzq.ioc.support.SimpleInstantiationStrategy;
import com.xzq.util.ObjectUtil;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 自动填充属性的beanFactory
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:50
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    //默认使用cglib实例化策略
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, Object[] args, BeanDefinition beanDefinition) {
        Object bean = null;
        //1. 创建bean实例
        bean = createBeanInstance(beanName, args, beanDefinition);
        //2. 自动填充属性 （也是该抽象模版类最核心的功能）
        applyPropertyValues(beanName, bean, beanDefinition);
        //3. 执行bean 的初始化方法和BeanPostProcessor的前置后置处理方法
        bean=initializeBean(beanName, bean, beanDefinition);

        //4. 加入单例池中
        if (beanDefinition.isSingleton()) {
            addSingleton(beanName, bean);
        }
        return bean;
    }

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {

        //执行BeanPostProcessor before方法
        Object wrapperBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        //执行BeanPostProcessor after方法

        wrapperBean = applyBeanPostProcessorsAfterInitialization(wrapperBean, beanName);

        return wrapperBean;
    }

    private Object applyBeanPostProcessorsAfterInitialization(Object wrapperBean, String beanName) {
        Object result = wrapperBean;

        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);

            result = ObjectUtil.isNull(current) ? result : current;

        }
        return result;
    }

    private Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) {
        Object result = bean;

        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);

            result = ObjectUtil.isNull(current) ? result : current;

        }
        return result;
    }

    private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValueList = beanDefinition.getPropertyValueList();
        for (PropertyValue propertyValue : propertyValueList) {

            String name = propertyValue.getName();
            Object value = propertyValue.getValue();

            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getBeanName());
            }

            //反射填充属性
            BeanUtil.setFieldValue(bean, name, value);
        }
    }

    private Object createBeanInstance(String beanName, Object[] args, BeanDefinition beanDefinition) {

        Constructor constructorToUse = null;

        Class beanClass = beanDefinition.getBeanClass();

        Constructor[] declaredConstructors = beanClass.getDeclaredConstructors();

        if (args != null) {
            //根据参数列表寻找合适的构造函数
            for (Constructor ctor : declaredConstructors) {
                if (isConstructorByArgs(ctor, args)) {
                    constructorToUse = ctor;
                    break;
                }
            }
        }

        return instantiationStrategy.instantiate(beanDefinition, constructorToUse, args, beanName);
    }

    /**
     * 匹配参数列表是否和该构造函数的参数列表 是否匹配
     * @param ctor
     * @param args
     * @return
     */
    private boolean isConstructorByArgs(Constructor ctor, Object[] args) {
        Class[] parameterTypes = ctor.getParameterTypes();

        //1. 先判断参数个数是否匹配
        if (parameterTypes.length != args.length) {
            return Boolean.FALSE;
        }

        //2. 判断参数类型是否匹配
        for (int i = 0; i < parameterTypes.length; i++) {

            Class parameterType = parameterTypes[i];

            Object arg = args[i];

            if (!parameterType.isInstance(arg)) {
                return Boolean.FALSE;
            }

        }

        return Boolean.TRUE;
    }

}
