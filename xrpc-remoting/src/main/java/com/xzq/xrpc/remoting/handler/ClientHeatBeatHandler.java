package com.xzq.xrpc.remoting.handler;

import com.xzq.xrpc.remoting.message.PingMessage;
import com.xzq.xrpc.remoting.message.PongMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 客户端心跳检测处理器
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/20 10:42
 */
public class ClientHeatBeatHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(ClientHeatBeatHandler.class);

    /**
     * 读取服务端 Pong响应
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof PongMessage) {
            logger.info("[client accept heartbeat Pong] remoteAddress:{}  ", ctx.channel().remoteAddress().toString());
        }else{
            //责任链，传递下一个Handler处理
            ctx.fireChannelRead(msg);
        }

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {

            IdleStateEvent idleEvent = (IdleStateEvent) evt;

            switch (idleEvent.state()) {
                case WRITER_IDLE:
                    sendPing(ctx);
                    break;
                case READER_IDLE:
                    closeChannel(ctx);
                case ALL_IDLE:
                    closeChannel(ctx);
                default:
                    break;
            }
        }
    }

    private void closeChannel(ChannelHandlerContext ctx) {
        logger.info("client close channel ,remoteAddress:{}", ctx.channel().remoteAddress());
        ctx.disconnect();
        ctx.close();
    }

    private void sendPing(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new PingMessage());
    }
}
