package com.xzq.spring;

import com.xzq.client.proxy.ProxyFactory;
import com.xzq.ioc.BeansException;
import com.xzq.ioc.annotation.XrpcAutowired;
import com.xzq.register.Register;
import com.xzq.register.model.ProviderService;
import com.xzq.util.CollectionUtil;
import com.xzq.util.ObjectUtil;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/14 12:04
 */
public class XrpcSpringBeanPostProcessor implements BeanPostProcessor {

    private ProxyFactory proxyFactory;

    private Register register;


    public XrpcSpringBeanPostProcessor(ProxyFactory proxyFactory, Register register) {
        this.proxyFactory = proxyFactory;
        this.register = register;
    }

    /**
     * 初始化bean之前调用方法
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {


        Field[] fields = bean.getClass().getDeclaredFields();

        //1. 校验bean是否有@XrpcAutuwired字段
        List<Field> hasXrpcAutowiredAnnoFields = Arrays
                .stream(fields)
                .filter(field -> ObjectUtil.notNull(field.getAnnotation(XrpcAutowired.class)))
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(hasXrpcAutowiredAnnoFields)) {
            return bean;
        }

        for (Field field : hasXrpcAutowiredAnnoFields) {

            //2. pull serviceProviders
            List<ProviderService> services = register.findServices(field.getType().getName());

            if (CollectionUtil.isEmpty(services)) continue;

            //3. 默认使用第一个服务，TODO 后续补充负载均衡策略
            ProviderService providerService = services.get(0);

            //3. 使用代理工厂生成XrpcClient 代理对象
            Object proxyXrpcClient = proxyFactory.getProxy(field.getType(), providerService.getHost(), providerService.getPort());

            //4. 将代理对象依赖注入进bean
            try {
                field.setAccessible(true);

                field.set(bean, proxyXrpcClient);

            } catch (IllegalAccessException e) {continue;}
        }

        return bean;
    }
}
