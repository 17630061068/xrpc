package com.xzq.server;

import com.xzq.server.factory.ProviderFactory;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:53
 */
public class XrpcServer {

    private Logger logger = LoggerFactory.getLogger(XrpcServer.class);

    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;

    private XrpcProtocol xrpcProtocol;

    private ProviderFactory providerFactory;

    private Channel channel;


    public XrpcServer(NioEventLoopGroup boss, NioEventLoopGroup worker, XrpcProtocol xrpcProtocol, ProviderFactory providerFactory) {
        this.boss = boss;
        this.worker = worker;
        this.xrpcProtocol = xrpcProtocol;
        this.providerFactory = providerFactory;
    }

    public void lister(int port) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new XrpcServerChannelInitializer(xrpcProtocol, providerFactory));

        try {
            ChannelFuture cf = serverBootstrap.bind(new InetSocketAddress(port)).sync();
            channel = cf.channel();
            logger.info(">>>>> xrpc server start success");
        } catch (InterruptedException e) {
            logger.info(">>>>> xrpc server start fail");
            throw new RuntimeException(e);
        }
    }
}
