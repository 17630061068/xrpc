package com.xzq.server;


import com.alibaba.fastjson2.JSONObject;
import com.xzq.server.factory.DefaultProviderFactory;
import com.xzq.xrpc.remoting.message.Message;
import com.xzq.xrpc.remoting.message.XrpcRequestMessage;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import com.xzq.xrpc.remoting.serializer.Serializer;
import com.xzq.xrpc.remoting.serializer.SerializerUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;


class XrpcServerTest {


//    public static void main(String[] args) {
//
//        NioEventLoopGroup boss = new NioEventLoopGroup();
//        NioEventLoopGroup worker = new NioEventLoopGroup();
//        XrpcProtocol xrpcProtocol = XrpcProtocol.DEFAULT_PROTOCOL();
//
//        DefaultProviderFactory defaultProviderFactory = new DefaultProviderFactory();
//
//        defaultProviderFactory.addProvider(UserClient.class.getName(), new UserClientImpl());
//
//        XrpcServer xrpcServer = new XrpcServer(boss,worker,xrpcProtocol,defaultProviderFactory);
//        xrpcServer.lister(9999);
//    }

//    public static void main(String[] args) throws InterruptedException {
//        ServerBootstrap serverBootstrap = new ServerBootstrap();
//
//        ChannelFuture cf = serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
//                .channel(NioServerSocketChannel.class)
//                .option(ChannelOption.SO_BACKLOG, 128)
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel sc) throws Exception {
//
//                        sc.pipeline().addLast(new LengthFieldBasedFrameDecoder(
//                                1024 * 10, 7, 4, 0, 0
//                        ));
//                        sc.pipeline().addLast(new ByteToMessageDecoder() {
//                            @Override
//                            protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
//
//
//                                //4字节魔数
//                                int magicNum = byteBuf.readInt();
//                                //1字节版本号
//                                byte version = byteBuf.readByte();
//                                //1字节序列化
//                                byte serializerType = byteBuf.readByte();
//                                //1字节消息指令类型
//                                byte messageType = byteBuf.readByte();
//
//                                //解析数据长度
//                                int dataLength = byteBuf.readInt();
//
//                                byte[] data = new byte[dataLength];
//
//                                byteBuf.readBytes(data);
//
//                                XrpcRequestMessage message = SerializerUtil.PROTOBUF().deserializer(data, XrpcRequestMessage.class);
//
//                                list.add(message);
//                            }
//                        });
//
//                        sc.pipeline().addLast(new SimpleChannelInboundHandler<XrpcRequestMessage>() {
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, XrpcRequestMessage s) throws Exception {
//                                System.out.println(JSONObject.toJSONString(s));
//                            }
//                        });
//                    }
//                })
//                .bind(9999)
//                .sync();
//    }

}