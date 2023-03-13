package com.xzq.bootstrap;

import com.xzq.client.proxy.ProxyFactory;
import com.xzq.ioc.annotation.XrpcService;
import com.xzq.ioc.bean.BeanDefinition;
import com.xzq.ioc.bean.PropertyValue;
import com.xzq.ioc.context.ClassPathApplicationContext;
import com.xzq.register.RedisRegister;
import com.xzq.register.Register;
import com.xzq.register.config.RegisterConfig;
import com.xzq.register.model.ProviderService;
import com.xzq.server.XrpcServer;
import com.xzq.server.factory.DefaultProviderFactory;
import com.xzq.server.factory.ProviderFactory;
import com.xzq.util.CollectionUtil;
import com.xzq.util.IocUtil;
import com.xzq.util.IpUtil;
import com.xzq.util.ObjectUtil;
import com.xzq.xrpc.remoting.config.MessageCodecConfig;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import com.xzq.xrpc.remoting.protocol.XrpcProtocolV1;
import io.netty.channel.nio.NioEventLoopGroup;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优化点：
 *         beanFactory的 ioc,di 功能不完善
 *         代理功能应该移交ioc容器实现
 *
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 19:25
 */
public class XrpcServerBootStrap {

    public XrpcServerBootStrap(RegisterConfig registerConfig, String classPath) {
        this.registerConfig = registerConfig;
        this.classPath = classPath;
    }

    private NioEventLoopGroup boss;

    private NioEventLoopGroup worker;

    private MessageCodecConfig messageCodecConfig;

    private ClassPathApplicationContext applicationContext;

    private RegisterConfig registerConfig;

    private String classPath;

    private Integer serverPort;

    /**
     * 是否启动服务
     */
    private Boolean enableServer;

    private XrpcProtocol xrpcProtocol;

    private ProviderFactory providerFactory;

    private static final String DEFAULT_CLASS_PATH = XrpcServerBootStrap.class.getPackage().getName();


    public void start() {

        //初始化
        init();

        //1. 启动xrpc netty服务
        startServer();

        //2. 暴露服务
        exportServers();

    }

    private void init() {
        if (ObjectUtil.isNull(boss)) boss = new NioEventLoopGroup();

        if (ObjectUtil.isNull(worker)) worker = new NioEventLoopGroup();

        if (ObjectUtil.isNull(xrpcProtocol)) xrpcProtocol = new XrpcProtocolV1();

        if (ObjectUtil.isNull(messageCodecConfig)) messageCodecConfig = new MessageCodecConfig(xrpcProtocol);

        if (ObjectUtil.isNull(serverPort)) serverPort = 20001;

        if (ObjectUtil.isNull(enableServer)) enableServer = Boolean.FALSE;

        //IOC容器
        if (ObjectUtil.isNull(applicationContext)) applicationContext= new ClassPathApplicationContext(classPath);

        //注册中心和代理工厂注册IOC
        initBeanFactory(applicationContext);

        //扫描自身应用的路径
        applicationContext.scanBeanByClassPath(DEFAULT_CLASS_PATH);
        //启动ioc容器
        applicationContext.refresh();

        //提供者工厂
        if (ObjectUtil.isNull(providerFactory)) providerFactory = new DefaultProviderFactory(applicationContext);

    }

    private void initBeanFactory(ClassPathApplicationContext applicationContext) {
        //注册中心 bean定义
        BeanDefinition registerBeanDefinition = new BeanDefinition();
        registerBeanDefinition.setBeanName(IocUtil.getDefaultBeanName(RedisRegister.class));
        registerBeanDefinition.setBeanClass(RedisRegister.class);
        List<PropertyValue> propertyValueList = new ArrayList<>();
        PropertyValue hostPropertyValue = new PropertyValue();
        PropertyValue portPropertyValue = new PropertyValue();
        PropertyValue authPropertyValue = new PropertyValue();

        hostPropertyValue.setName("host");
        hostPropertyValue.setValue(registerConfig.getHost());
        portPropertyValue.setName("port");
        portPropertyValue.setValue(registerConfig.getPort());
        authPropertyValue.setName("auth");
        authPropertyValue.setValue(registerConfig.getAuth());

        propertyValueList.add(hostPropertyValue);
        propertyValueList.add(portPropertyValue);
        propertyValueList.add(authPropertyValue);

        registerBeanDefinition.setPropertyValueList(propertyValueList);

        //代理工厂bean定义
        BeanDefinition proxyFactoryBeanDefinition = new BeanDefinition();
        proxyFactoryBeanDefinition.setBeanName(IocUtil.getDefaultBeanName(ProxyFactory.class));
        proxyFactoryBeanDefinition.setBeanClass(ProxyFactory.class);

        List<PropertyValue> propertyValues = new ArrayList<>();
        PropertyValue propertyValue = new PropertyValue();
        propertyValue.setName("xrpcProtocol");
        propertyValue.setValue(new XrpcProtocolV1());
        propertyValues.add(propertyValue);
        proxyFactoryBeanDefinition.setPropertyValueList(propertyValues);

        applicationContext.registerBeanDefinition(registerBeanDefinition);

        applicationContext.registerBeanDefinition(proxyFactoryBeanDefinition);
    }


    private void startServer() {
        if (enableServer) {
            XrpcServer xrpcServer = new XrpcServer(boss, worker, xrpcProtocol, providerFactory);
            xrpcServer.lister(serverPort);
        }
    }

    private void exportServers() {
        //从bean工厂获取XrpcService注解的bean
        List<Object> beans = applicationContext.getBeansWithAnnotation(XrpcService.class);

        if (CollectionUtil.notEmpty(beans)) {
            //包装为服务提供者
            List<ProviderService> providerServices = wrapperService(beans);
            //注册服务提供者
            Register register = getRegister();
            register.init();
            getRegister().register(providerServices);
        }
    }

    private List<ProviderService> wrapperService(List<Object> beans) {

        return beans.stream().map(bean -> {

            ProviderService providerService = new ProviderService();

            InetSocketAddress address = new InetSocketAddress(serverPort);

            Class<?>[] interfaces = bean.getClass().getInterfaces();
            String serviceName = interfaces[0].getName();


            providerService.setServiceName(serviceName);
            //                providerService.setHost(InetAddress.getLocalHost().getHostAddress());
            providerService.setHost(IpUtil.getPublicIP());
            providerService.setPort(address.getPort());

            return providerService;
        }).collect(Collectors.toList());
    }

    private Register getRegister() {
       return applicationContext.getBeansOfType(Register.class).get(0);
    }

    public void setEnableServer(Boolean enableServer) {
        this.enableServer = enableServer;
    }

    public ClassPathApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
