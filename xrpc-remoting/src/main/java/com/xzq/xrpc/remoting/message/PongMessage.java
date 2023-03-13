package com.xzq.xrpc.remoting.message;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 16:11
 */
public class PongMessage extends Message {

    @Override
    public int getMessageType() {
        return Message.PongMessage;
    }

}
