package com.xzq.ioc.factory;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 19:04
 */
public interface BeanFactory {

    Object getBean(String beanName);
    Object getBean(String beanName,Object[] args);

    <T> List<T> getBeansOfType(Class<T> clazz);

    List<Object> getBeansWithAnnotation(Class<? extends Annotation> anno);



}
