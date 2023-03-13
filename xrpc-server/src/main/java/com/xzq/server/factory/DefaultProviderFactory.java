package com.xzq.server.factory;

import com.xzq.ioc.context.ClassPathApplicationContext;
import com.xzq.ioc.factory.BeanFactory;
import com.xzq.server.beans.ProviderReference;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 14:29
 */
public class DefaultProviderFactory implements ProviderFactory {

    private ClassPathApplicationContext applicationContext;

    private Map<String, Object> providerMap = new ConcurrentHashMap<>();

    public DefaultProviderFactory(ClassPathApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object getProvider(String interfaceName) {

        try {
            return applicationContext.getBeansOfType(Class.forName(interfaceName)).get(0);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addProvider(String interfaceName,Object provider) {
        providerMap.put(interfaceName, provider);
    }

}
