package com.xzq.server;

import com.xzq.server.factory.ProviderFactory;
import com.xzq.xrpc.remoting.codec.MessageCodec;
import com.xzq.xrpc.remoting.codec.ProtocolFrameDecoder;
import com.xzq.xrpc.remoting.handler.ServerHeatBeatHandler;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:56
 */
public class XrpcServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private XrpcProtocol xrpcProtocol;

    private ProviderFactory providerFactory;

    public XrpcServerChannelInitializer(XrpcProtocol xrpcProtocol, ProviderFactory providerFactory) {
        this.xrpcProtocol = xrpcProtocol;
        this.providerFactory = providerFactory;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline()
                //空闲检测处理器 （搭配心跳检测处理器使用）
                .addLast(new IdleStateHandler(15, 0, 0, TimeUnit.SECONDS))
                //半包粘包解码器
                .addLast(new ProtocolFrameDecoder())
                //消息编解码器
                .addLast(new MessageCodec(xrpcProtocol))
                //心跳检测处理器
                .addLast(new ServerHeatBeatHandler())
                //RPC消息处理器
                .addLast(new XrpcServerMessageHandler(providerFactory));

    }

}
