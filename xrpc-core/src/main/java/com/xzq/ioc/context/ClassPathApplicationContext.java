package com.xzq.ioc.context;

import com.xzq.ioc.DefaultBeanFactoryImpl;
import com.xzq.ioc.annotation.Autowired;
import com.xzq.ioc.annotation.Service;
import com.xzq.ioc.annotation.XrpcAutowired;
import com.xzq.ioc.annotation.XrpcService;
import com.xzq.ioc.bean.BeanDefinition;
import com.xzq.ioc.bean.BeanPostProcessor;
import com.xzq.ioc.bean.BeanReference;
import com.xzq.ioc.bean.PropertyValue;
import com.xzq.ioc.factory.BeanFactory;
import com.xzq.ioc.support.BeanDefinitionRegister;
import com.xzq.util.ClassUtil;
import com.xzq.util.IocUtil;
import com.xzq.util.ListUtil;
import com.xzq.util.ObjectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Desc 类路径扫描上下文
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 09:39
 */
public class ClassPathApplicationContext implements BeanFactory, BeanDefinitionRegister {

    private String classPath;

    private DefaultBeanFactoryImpl beanFactory=new DefaultBeanFactoryImpl();

    public ClassPathApplicationContext(String classPath) {

        this.classPath = classPath;

//        refresh();

    }

    public void refresh() {

        //1. bean扫描 只是将bean的定义装载 ，不涉及实例化
        scanBeanByClassPath(classPath);

        //2. 注册beanPostProcess  需要提前于其他Bean对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);



    }

    private void registerBeanPostProcessors(DefaultBeanFactoryImpl beanFactory) {
        List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }


    //根据类路径扫描 XrpcService对象
    public void scanBeanByClassPath(String classPath) {
        Set<Class<?>> beanClassSet = ClassUtil.scanPackage(classPath);


        //过滤掉不需要IOC的class
        List<Class<?>> iocClass = beanClassSet
                .stream()
                .filter(clazz ->
                        ObjectUtil.notNull(clazz.getAnnotation(Service.class)) ||
                        ObjectUtil.notNull(clazz.getAnnotation(XrpcService.class)) ||
                        clazz.equals(BeanPostProcessor.class)
                                )
                .collect(Collectors.toList());

        //将class 转换为beanDefinition
        List<BeanDefinition> beanDefinitions = iocClass.stream().map(clazz -> {

            BeanDefinition beanDefinition = new BeanDefinition();

            beanDefinition.setBeanClass(clazz);
            //bean 名称默认为类名称（首字母小写）
            beanDefinition.setBeanName(IocUtil.getDefaultBeanName(clazz));

            Field[] fields = clazz.getDeclaredFields();

            List<Field> autowiredBean = Arrays.stream(fields).filter(
                            field -> ObjectUtil.notNull(field.getAnnotation(Autowired.class))
//                                    || ObjectUtil.notNull(field.getAnnotation(XrpcAutowired.class))
                    )
                    .collect(Collectors.toList());

            List<PropertyValue> propertyValues = autowiredBean.stream().map(field -> {
                PropertyValue propertyValue = new PropertyValue();
                propertyValue.setName(field.getName());
                propertyValue.setValue(new BeanReference(IocUtil.getDefaultBeanName(field.getType())));
                return propertyValue;
            }).collect(Collectors.toList());

            beanDefinition.setPropertyValueList(propertyValues);
            return beanDefinition;
        }).collect(Collectors.toList());


        beanFactory.registerBeanDefinition(beanDefinitions);

    }

    @Override
    public Object getBean(String beanName) {
        return beanFactory.getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object[] args) {
        return beanFactory.getBean(beanName, args);
    }

    @Override
    public <T> List<T> getBeansOfType(Class<T> clazz) {
        return beanFactory.getBeansOfType(clazz);
    }

    @Override
    public List<Object> getBeansWithAnnotation(Class<? extends Annotation> anno) {
        return beanFactory.getBeansWithAnnotation(anno);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(ListUtil.toList(beanDefinition));
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanFactory.registerBeanDefinition(beanDefinition);
    }
}
