package com.xzq.xrpc.remoting.codec;

import com.xzq.xrpc.remoting.message.Message;
import com.xzq.xrpc.remoting.protocol.XrpcProtocol;
import com.xzq.xrpc.remoting.serializer.Serializer;
import com.xzq.xrpc.remoting.serializer.SerializerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * 消息编解码器
 *
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 16:20
 */
public class MessageCodec extends ByteToMessageCodec {


    private XrpcProtocol xrpcProtocol;


    public MessageCodec(XrpcProtocol xrpcProtocol) {
        this.xrpcProtocol = xrpcProtocol;
    }

    /**
     * 编码
     * @param channelHandlerContext
     * @param o
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        if (o instanceof Message) {
            Message message = (Message) o;
            //4字节魔数
            byteBuf.writeInt(xrpcProtocol.getMagicNum());
            //1字节版本号
            byteBuf.writeByte(xrpcProtocol.getVersion());
            //1字节序列化方式
            byteBuf.writeByte(xrpcProtocol.getSerializer());
            //1字节消息指令类型
            byteBuf.writeByte(message.getMessageType());

            byte[] data = SerializerUtil.match(xrpcProtocol.getSerializer()).serializer(message);

            //4字节消息长度
            byteBuf.writeInt(data.length);
            //消息体
            byteBuf.writeBytes(data);
        }
    }

    /**
     * 解码
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List list) throws Exception {
        //4字节魔数
        int magicNum = byteBuf.readInt();
        //1字节版本号
        byte version = byteBuf.readByte();
        //1字节序列化
        byte serializerType = byteBuf.readByte();
        //1字节消息指令类型
        byte messageType = byteBuf.readByte();

        Serializer serializer = SerializerUtil.match(serializerType);

        Class<? extends Message> message = Message.match(messageType);

        //检查当前协议是否支持
        if (!xrpcProtocol.isSupport(magicNum, version, (int)serializerType, (int)messageType)) {
            channelHandlerContext.close();
            return;
        }

        //解析数据长度
        int dataLength = byteBuf.readInt();

        byte[] data = new byte[dataLength];

        byteBuf.readBytes(data);

        Message messageBody = serializer.deserializer(data, message);

        list.add(messageBody);
    }

}
