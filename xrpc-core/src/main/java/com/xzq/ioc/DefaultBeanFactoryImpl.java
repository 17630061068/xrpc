package com.xzq.ioc;


import com.xzq.ioc.bean.BeanDefinition;
import com.xzq.ioc.factory.AbstractAutowireCapableBeanFactory;
import com.xzq.ioc.factory.AbstractBeanFactory;
import com.xzq.ioc.factory.BeanFactory;
import com.xzq.ioc.factory.BeanRegister;
import com.xzq.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 19:05
 */
public class DefaultBeanFactoryImpl extends AbstractAutowireCapableBeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    public void registerBeanDefinition(List<BeanDefinition> beanDefinitionList) {
        beanDefinitionList.forEach(obj -> {
            registerBeanDefinition(obj);
        });
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
    }

    @Override
    public <T> List<T> getBeansOfType(Class<T> clazz) {
        List<String> beanNames = getBeanNamesOfType(clazz);
        ArrayList<Object> instances = new ArrayList<>();
        beanNames.forEach(beanName -> {
            Object instance = this.getBean(beanName);
            instances.add(instance);
        });
        return (List<T>) instances;
    }

    @Override
    public List<Object> getBeansWithAnnotation(Class<? extends Annotation> anno) {
        List<String> beanNames = getBeanNamesOfAnnotation(anno);
        ArrayList<Object> instances = new ArrayList<>();
        beanNames.forEach(beanName -> {
            Object instance = this.getBean(beanName);
            instances.add(instance);
        });
        return instances;
    }

    private List<String> getBeanNamesOfAnnotation(Class<? extends Annotation> anno) {
        ArrayList<String> beanNames = new ArrayList<>();
        this.beanDefinitionMap.entrySet().stream().forEach(
                (entry) -> {
                    Class beanClass = entry.getValue().getBeanClass();

                    if (ObjectUtil.notNull(beanClass.getAnnotation(anno))) beanNames.add(entry.getKey());
                }
        );
        return beanNames;
    }

    private List<String> getBeanNamesOfType(Class<?> clazz) {
        ArrayList<String> beanNames = new ArrayList<>();
        this.beanDefinitionMap.entrySet().stream().forEach(
                (entry) -> {
                    Class beanClass = entry.getValue().getBeanClass();
                    if (beanClass.equals(clazz) || clazz.isAssignableFrom(beanClass)) beanNames.add(entry.getKey());
                }
        );
        return beanNames;
    }

}
