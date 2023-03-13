package com.xzq.bootstrap.ioc;

import com.xzq.client.proxy.ProxyFactory;
import com.xzq.ioc.BeansException;
import com.xzq.ioc.annotation.Autowired;
import com.xzq.ioc.annotation.Service;
import com.xzq.ioc.annotation.XrpcAutowired;
import com.xzq.ioc.bean.BeanPostProcessor;
import com.xzq.register.RedisRegister;
import com.xzq.register.Register;
import com.xzq.register.model.ProviderService;
import com.xzq.util.CollectionUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 12:20
 */
@Service
public class XrpcClientBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    private RedisRegister register;

    @Autowired
    private ProxyFactory proxyFactory;

    public XrpcClientBeanPostProcessor() {
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            XrpcAutowired annotation = field.getAnnotation(XrpcAutowired.class);
            if (annotation != null) {
                //寻找服务
                String serviceName = field.getType().getName();
                List<ProviderService> providerServices = register.findServices(serviceName);

                if (CollectionUtil.notEmpty(providerServices)) {
                    ProviderService providerService = providerServices.get(0);
                    Object proxyClient = proxyFactory.getProxy(field.getType(), providerService.getHost(), providerService.getPort());

                    try {
                        field.setAccessible(Boolean.TRUE);
                        field.set(bean, proxyClient);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


        return bean;
    }
}
