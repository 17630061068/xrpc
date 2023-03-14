package com.xzq.spring;

import com.xzq.client.XrpcMessageHandler;
import com.xzq.client.proxy.ProxyFactory;
import com.xzq.register.RedisRegister;
import com.xzq.register.Register;
import com.xzq.register.config.RegisterConfig;
import com.xzq.server.XrpcServer;
import com.xzq.server.factory.ProviderFactory;
import com.xzq.spring.properties.RegisterProperties;
import com.xzq.spring.properties.ServerProperties;
import com.xzq.spring.properties.XrpcProperties;
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

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 23:29
 */
@EnableConfigurationProperties({
        XrpcProperties.class,
        RegisterProperties.class,
        ServerProperties.class
})
@Configuration
public class XrpcAutoConfiguration {

    private XrpcProperties xrpcProperties;


    public XrpcAutoConfiguration(XrpcProperties xrpcProperties) {
        this.xrpcProperties = xrpcProperties;
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
        RegisterConfig registerConfig = new RegisterConfig(
                xrpcProperties.getRegister().getRedis().getHost(),
                xrpcProperties.getRegister().getRedis().getPort(),
                xrpcProperties.getRegister().getRedis().getAuth()
        );
        RedisRegister redisRegister = new RedisRegister(registerConfig);
        redisRegister.init();
        return redisRegister;
    }


    @Bean
    public XrpcSpringBeanFactoryAware xrpcSpringBeanFactoryAware() {
        return new XrpcSpringBeanFactoryAware(xrpcProperties, xrpcServer(), register());
    }

    @Bean
    public ProxyFactory proxyFactory() {
        return new ProxyFactory(xrpcProtocol(), bootstrap());
    }

    @Bean
    public XrpcSpringBeanPostProcessor xrpcSpringBeanPostProcessor() {
        return new XrpcSpringBeanPostProcessor(proxyFactory(), register());
    }


}
