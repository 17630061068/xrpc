package com.xzq.client;

import com.xzq.xrpc.remoting.message.XrpcResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:31
 */
public class XrpcMessageHandler extends SimpleChannelInboundHandler<XrpcResponseMessage> {

    Logger logger = LoggerFactory.getLogger(XrpcMessageHandler.class);

    //用来接收结果的 promise 对象
    public static final Map<ChannelId, Promise<Object>> PROMISES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, XrpcResponseMessage xrpcResponseMessage) throws Exception {

        logger.info("{}", xrpcResponseMessage);

        Promise<Object> promise = PROMISES.remove(ctx.channel().id());

        if (promise != null) {
            Object returnValue = xrpcResponseMessage.getReturnValue();
            Throwable throwable = xrpcResponseMessage.getThrowable();

            if (throwable != null) {
                promise.setFailure(throwable);
            }else {
                promise.setSuccess(returnValue);
            }
        }

    }
}
