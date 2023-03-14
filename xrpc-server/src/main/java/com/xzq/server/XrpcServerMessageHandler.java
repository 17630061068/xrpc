package com.xzq.server;

import com.xzq.server.factory.ProviderFactory;
import com.xzq.xrpc.remoting.message.XrpcRequestMessage;
import com.xzq.xrpc.remoting.message.XrpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:59
 */
@ChannelHandler.Sharable
public class XrpcServerMessageHandler extends SimpleChannelInboundHandler<XrpcRequestMessage> {

    private Logger logger = LoggerFactory.getLogger(XrpcServerMessageHandler.class);

    private ProviderFactory providerFactory;

    public XrpcServerMessageHandler(ProviderFactory providerFactory) {
        this.providerFactory = providerFactory;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, XrpcRequestMessage xrpcRequestMessage) throws Exception {
        XrpcResponseMessage xrpcResponseMessage = new XrpcResponseMessage();

        logger.info("accept rpc request,address:{},request:{}",ctx.channel().localAddress().toString(),xrpcRequestMessage);
        try {
            //从IOC容器寻找
            Object provider = providerFactory.getProvider(xrpcRequestMessage.getInterfaceName());
            Method method = provider.getClass().getMethod(xrpcRequestMessage.getMethodName(), xrpcRequestMessage.getParameterTypes());
            Object result = method.invoke(provider, xrpcRequestMessage.getParameterValues());
            xrpcResponseMessage.setReturnValue(result);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            xrpcResponseMessage.setThrowable(e);
        }

        ctx.writeAndFlush(xrpcResponseMessage);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel sc = (SocketChannel) ctx.channel();
        logger.info("连接建立.....");
        logger.info("连接ip: {}  连接端口: {}", sc.localAddress().getHostString(), sc.localAddress().getPort());
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("连接异常:\r\n");
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("连接断开..... 连接信息:{}", ctx.channel().localAddress().toString());
    }
}
