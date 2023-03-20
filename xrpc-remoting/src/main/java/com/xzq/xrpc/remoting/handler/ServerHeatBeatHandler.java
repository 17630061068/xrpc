package com.xzq.xrpc.remoting.handler;

import com.xzq.xrpc.remoting.message.PingMessage;
import com.xzq.xrpc.remoting.message.PongMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 服务段心跳检测处理器，接受Ping 消息，返回Pong
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/20 10:42
 */
@Slf4j
public class ServerHeatBeatHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(ServerHeatBeatHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PingMessage) {
            logger.info("[Server accept heartbeat Ping] remoteAddress:{} ", ctx.channel().remoteAddress());
            sendPong(ctx);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleEvent = (IdleStateEvent) evt;
            if (idleEvent.state() == IdleState.READER_IDLE) {
                closeChannel(ctx);
            }
        }
    }

    private void closeChannel(ChannelHandlerContext ctx) {
        logger.info("server close channel , remoteAddress:{}",ctx.channel().remoteAddress().toString());
        ctx.disconnect();
        ctx.close();
    }

    private void sendPong(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new PongMessage());
    }


}
