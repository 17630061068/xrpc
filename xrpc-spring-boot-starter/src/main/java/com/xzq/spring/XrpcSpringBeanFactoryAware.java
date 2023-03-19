package com.xzq.spring;

import com.xzq.ioc.annotation.XrpcService;
import com.xzq.register.Register;
import com.xzq.register.model.ProviderService;
import com.xzq.server.XrpcServer;
import com.xzq.spring.properties.XrpcProperties;
import com.xzq.util.IpUtil;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/14 11:18
 */
public class XrpcSpringBeanFactoryAware implements BeanFactoryAware, InitializingBean {


    private Logger logger = LoggerFactory.getLogger(XrpcSpringBeanFactoryAware.class);


    private XrpcProperties xrpcProperties;

    private XrpcServer xrpcServer;

    private Register register;

    public XrpcSpringBeanFactoryAware(XrpcProperties xrpcProperties, XrpcServer xrpcServer, Register register) {
        this.xrpcProperties = xrpcProperties;
        this.xrpcServer = xrpcServer;
        this.register = register;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

        if (xrpcProperties.getServer().getEnable()) {//只有开启了提供者服务，才可以暴露服务

            //1.  从spring ioc 容器中 获取所有被@XrpcService标注的bean
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;

            Map<String, Object> providerBeans = listableBeanFactory.getBeansWithAnnotation(XrpcService.class);

            //2. 包装为提供者类型
            List<ProviderService> providerServices = wrapperProvider(providerBeans);

            //3. 使用注册中心暴露服务
            register.register(providerServices);

            //4. 打印注册成功日志
            providerServices.forEach(providerService -> {
                logger.info("Xrpc服务注册success , serviceName:{} ,host:{} ,port:{}",
                        providerService.getServiceName(),
                        providerService.getHost(),
                        providerService.getPort());
            });


        }
    }

    private List<ProviderService> wrapperProvider(Map<String, Object> providerBeans) {
        return providerBeans.values().stream().map((obj) -> {
            ProviderService providerService = new ProviderService();
            Class<?> serviceInterface = obj.getClass().getInterfaces()[0];
            providerService.setServiceName(serviceInterface.getName());
            //Ip地址
            String ip = xrpcProperties.getServer().getEnablePublicIp() ?
                    IpUtil.getPublicIp() : IpUtil.getIp();
            providerService.setHost(ip);
            //xrpc服务器端口
            providerService.setPort(xrpcProperties.getServer().getPort());
            return providerService;
        }).collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (xrpcProperties.getServer().getEnable()) {
            //启动xrpc netty服务
            ChannelFuture cf = xrpcServer.lister(xrpcProperties.getServer().getPort());

//            if (cf.isSuccess()) {
//                cf.channel().eventLoop().submit(() -> {
//                    try {
//                        //xrpc 服务端 关闭回调函数
//                        cf.channel().closeFuture().sync();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }
        }
    }
}
