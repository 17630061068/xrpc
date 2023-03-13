package com.xzq.xrpc.remoting.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

/**
 *  半包粘包解码器
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:12
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    /**
     * RPC协议:
     *      4字节 魔数
     *      1字节 版本号
     *      1字节 序列化方案
     *      1字节 消息指令类型
     *      4字节 长度
     *       正文长度
     */
    public ProtocolFrameDecoder() {
        this(1024 * 10, 7, 4, 0, 0);
    }

}
