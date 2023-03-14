package com.xzq.spring;

import com.xzq.server.factory.ProviderFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/14 13:06
 */
public class XrpcSpringProviderFactory implements ProviderFactory, BeanFactoryAware {

    private ListableBeanFactory beanFactory;

    @Override
    public Object getProvider(String interfaceName) {
        try {
            return beanFactory.getBean(Class.forName(interfaceName));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory)beanFactory;
    }
}
