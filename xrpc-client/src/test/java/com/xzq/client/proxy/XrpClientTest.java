package com.xzq.client.proxy;

import com.xzq.client.XrpcClient;
import com.xzq.client.XrpcMessageHandler;
import com.xzq.xrpc.remoting.codec.MessageCodec;
import com.xzq.xrpc.remoting.codec.ProtocolFrameDecoder;
import com.xzq.xrpc.remoting.message.Message;
import com.xzq.xrpc.remoting.message.XrpcRequestMessage;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import com.xzq.xrpc.remoting.serializer.SerializerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 16:22
 */
public class XrpClientTest {

    public static void main(String[] args) {

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

        XrpcClient xrpcClient = new XrpcClient(xrpcProtocol,bootstrap);


        try {

            xrpcClient.connect(new InetSocketAddress("127.0.0.1", 9999));

            System.out.println("连接成功>>>.");

            Class<UserClient> userClass = UserClient.class;

            Method testMethod = userClass.getMethod("test", String.class);

            XrpcRequestMessage request = XrpcRequestMessage.builder()
                    .interfaceName("com.xzq.server.UserClient")
                    .methodName(testMethod.getName())
                    .parameterTypes(testMethod.getParameterTypes())
                    .parameterValues(new Object[]{"你好"})
                    .returnType(testMethod.getReturnType())
                    .build();

            Object result = xrpcClient.invoke(request);

            System.out.println(result);

        } catch (InterruptedException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

//    public static void main(String[] args) throws InterruptedException, NoSuchMethodException {
//        ChannelFuture cf = new Bootstrap()
//                .group(new NioEventLoopGroup())
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.AUTO_READ, Boolean.TRUE)
//                .handler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel sc) throws Exception {
//
//                        sc.pipeline().addLast(new ByteToMessageCodec() {
//                            @Override
//                            protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf byteBuf) throws Exception {
//                                XrpcProtocol xrpcProtocol = XrpcProtocol.DEFAULT_PROTOCOL();
//
//                                //4字节魔数
//                                byteBuf.writeInt(xrpcProtocol.getMagicNum());
//                                //1字节版本号
//                                byteBuf.writeByte(xrpcProtocol.getVersion());
//                                //1字节序列化方式
//                                byteBuf.writeByte(xrpcProtocol.getSerializer());
//                                //1字节消息指令类型
//                                byteBuf.writeByte(Message.XrpcRequestMessage);
//
//                                byte[] data = SerializerUtil.PROTOBUF().serializer(o);
//
//                                //4字节消息长度
//                                byteBuf.writeInt(data.length);
//                                //消息体
//                                byteBuf.writeBytes(data);
//
//                            }
//
//                            @Override
//                            protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
//
//                            }
//                        });
//                    }
//                }).connect(new InetSocketAddress("127.0.0.1", 9999)).sync();
//
//        Channel channel = cf.channel();
//
//
//        Class<UserClient> userClass = UserClient.class;
//
//        Method testMethod = userClass.getMethod("test", String.class);
//
//        XrpcRequestMessage request = XrpcRequestMessage.builder()
//                .interfaceName(userClass.getName())
//                .methodName(testMethod.getName())
//                .parameterTypes(testMethod.getParameterTypes())
//                .parameterValues(args)
//                .returnType(testMethod.getReturnType())
//                .build();
//        channel.writeAndFlush(request);
//
//    }
}
