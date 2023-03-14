package com.xzq.client.proxy;

import com.xzq.client.XrpcClientConfig;
import com.xzq.client.XrpcMessageHandler;
import com.xzq.xrpc.remoting.codec.MessageCodec;
import com.xzq.xrpc.remoting.codec.ProtocolFrameDecoder;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProxyFactoryTest {


    @Test
    public void test_xrpcClient() {
        Bootstrap bootstrap = new Bootstrap();
        XrpcProtocol xrpcProtocol = XrpcProtocol.DEFAULT_PROTOCOL();

        bootstrap
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.AUTO_READ, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //半包粘包解码器
                        socketChannel.pipeline().addLast(new ProtocolFrameDecoder());
                        //消息编解码器
                        socketChannel.pipeline().addLast(new MessageCodec(xrpcProtocol));
                        //消息处理器
                        socketChannel.pipeline().addLast(new XrpcMessageHandler());
                    }
                });


        ProxyFactory proxyFactory = new ProxyFactory(XrpcProtocol.DEFAULT_PROTOCOL(), bootstrap, new XrpcClientConfig());

        UserClient userClient = proxyFactory.getProxy(UserClient.class, "127.0.0.1", 9999);

        System.out.println(userClient);

        userClient.test("123456");


    }

}