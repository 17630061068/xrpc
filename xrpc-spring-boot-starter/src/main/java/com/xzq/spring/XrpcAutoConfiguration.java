package com.xzq.spring;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.xzq.client.XrpcClientConfig;
import com.xzq.client.XrpcMessageHandler;
import com.xzq.client.proxy.ProxyFactory;
import com.xzq.register.NacosRegister;
import com.xzq.register.RedisRegister;
import com.xzq.register.Register;
import com.xzq.register.config.RegisterConfig;
import com.xzq.server.XrpcServer;
import com.xzq.server.factory.ProviderFactory;
import com.xzq.spring.logo.XrpcLogo;
import com.xzq.spring.properties.ClientProperties;
import com.xzq.spring.properties.RegisterProperties;
import com.xzq.spring.properties.ServerProperties;
import com.xzq.spring.properties.XrpcProperties;
import com.xzq.util.ObjectUtil;
import com.xzq.xrpc.remoting.codec.MessageCodec;
import com.xzq.xrpc.remoting.codec.ProtocolFrameDecoder;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import com.xzq.xrpc.remoting.protocol.XrpcProtocolV1;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 23:29
 */
@EnableConfigurationProperties({
        XrpcProperties.class,
        RegisterProperties.class,
        ServerProperties.class,
        ClientProperties.class
})
@Configuration
public class XrpcAutoConfiguration {

    private XrpcProperties xrpcProperties;


    public XrpcAutoConfiguration(XrpcProperties xrpcProperties) {
        this.xrpcProperties = xrpcProperties;

        XrpcLogo xrpcLogo = new XrpcLogo();
        xrpcLogo.println();

    }

    @Bean
    public XrpcLogo xrpcLogo() {
        return new XrpcLogo();
    }

    @Bean("bossGroup")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup();
    }

    @Bean("workerGroup")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    public Bootstrap bootstrap() {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                .group(workerGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.AUTO_READ, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //半包粘包解码器
                        socketChannel.pipeline().addLast(new ProtocolFrameDecoder());
                        //消息编解码器
                        socketChannel.pipeline().addLast(new MessageCodec(xrpcProtocol()));
                        //消息处理器
                        socketChannel.pipeline().addLast(new XrpcMessageHandler());
                    }
                });
        return bootstrap;
    }

    @Bean
    public XrpcProtocol xrpcProtocol() {
        return new XrpcProtocolV1();
    }

    @Bean
    public ProviderFactory providerFactory() {
        return new XrpcSpringProviderFactory();
    }

    @Bean
    public XrpcServer xrpcServer() {
        return new XrpcServer(bossGroup(), workerGroup(), xrpcProtocol(), providerFactory());
    }

    @Bean
    public Register register() {
        Register register = null;
        try {
            Class<?> clazz = Class.forName("com.alibaba.nacos.api.naming.NamingService");
            if (ObjectUtil.notNull(clazz) && ObjectUtil.notNull(xrpcProperties.getRegister().getNacos())) {
                Properties properties = new Properties();
                properties.setProperty("serverAddr", xrpcProperties.getRegister().getNacos().getServeraddr());
                properties.setProperty("namespace", xrpcProperties.getRegister().getNacos().getNamespace());
                if (ObjectUtil.notNull(xrpcProperties.getRegister().getNacos().getUserName())) {
                    properties.setProperty("username",xrpcProperties.getRegister().getNacos().getUserName());
                }
                if (ObjectUtil.notNull(xrpcProperties.getRegister().getNacos().getPassword())) {
                    properties.setProperty("password", xrpcProperties.getRegister().getNacos().getPassword());
                }
                NamingService namingService = NacosFactory.createNamingService(properties);
                register = new NacosRegister(namingService);
            }else{
                RegisterConfig registerConfig = new RegisterConfig(
                        xrpcProperties.getRegister().getRedis().getHost(),
                        xrpcProperties.getRegister().getRedis().getPort(),
                        xrpcProperties.getRegister().getRedis().getAuth()
                );
                register = new RedisRegister(registerConfig);
                register.init();
            }
        } catch (ClassNotFoundException | NacosException e) {
            throw new RuntimeException(e);
        }

        return register;
    }


    @Bean
    public XrpcSpringBeanFactoryAware xrpcSpringBeanFactoryAware() {
        return new XrpcSpringBeanFactoryAware(xrpcProperties, xrpcServer(), register());
    }

    @Bean
    public ProxyFactory proxyFactory() {

        XrpcClientConfig xrpcClientConfig = new XrpcClientConfig();
        if (ObjectUtil.notNull(xrpcProperties.getClient())) {
            xrpcClientConfig.setKeepAliveTime(xrpcProperties.getClient().getKeepAliveTime());
        }

        return new ProxyFactory(xrpcProtocol(), bootstrap(), xrpcClientConfig);
    }

    @Bean
    public XrpcSpringBeanPostProcessor xrpcSpringBeanPostProcessor() {
        return new XrpcSpringBeanPostProcessor(proxyFactory(), register());
    }


}
