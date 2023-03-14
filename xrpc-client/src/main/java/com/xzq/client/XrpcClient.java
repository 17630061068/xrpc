package com.xzq.client;

import com.xzq.xrpc.remoting.codec.MessageCodec;
import com.xzq.xrpc.remoting.codec.ProtocolFrameDecoder;
import com.xzq.xrpc.remoting.config.MessageCodecConfig;
import com.xzq.xrpc.remoting.message.Message;
import com.xzq.xrpc.remoting.message.XrpcRequestMessage;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.ReentrantLock;

/**
 * xrpc客户端
 *
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:24
 */
public class XrpcClient {

    /**
     * 连接锁
     */
    private ReentrantLock connectionLock = new ReentrantLock();
    /**
     * 协议
     */
    private XrpcProtocol xrpcProtocol;

    /**
     * netty 客户端服务器
     */
    private Bootstrap bootstrap;
    /**
     * channel 通道
     */
    private Channel channel;

    /**
     * 连接状态 true=已连接, false=未连接
     */
    private Boolean isConnection = Boolean.FALSE;


    public XrpcClient(XrpcProtocol xrpcProtocol, Bootstrap bootstrap) {
        this.xrpcProtocol = xrpcProtocol;
        this.bootstrap = bootstrap;
    }

    public ChannelFuture connect(InetSocketAddress address) throws InterruptedException {

        ChannelFuture f = null;

        //双检锁
        if (!isConnection) {

            connectionLock.lock();
            try {
                if (!isConnection) {

                    f = bootstrap.connect(address).sync();

                    channel = f.channel();

                    isConnection = Boolean.TRUE;

                    return f;
                }
            } finally {
                connectionLock.unlock();
            }
        }

        return f;

    }

    public Object invoke(Message xrpcRequestMessage) throws InterruptedException {

        //RPC远程调用
        ChannelFuture cf = channel.writeAndFlush(xrpcRequestMessage);

        cf.addListener(e -> {
            if (!e.isSuccess()) {
                e.cause();
            }
        });

        //创建Promise对象，接受结果
        DefaultPromise<Object> promise = new DefaultPromise<>(channel.eventLoop());
        XrpcMessageHandler.PROMISES.put(channel.id(), promise);

        //同步等待promise结果
        promise.await();
        if (promise.isSuccess()) {
            return promise.getNow();
        }else{
            //调用失败
            throw new RuntimeException(promise.cause());
        }

    }


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
