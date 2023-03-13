package com.xzq.xrpc.remoting.protocol;

import com.xzq.xrpc.remoting.message.Message;
import com.xzq.xrpc.remoting.serializer.Serializer;

import java.util.HashSet;
import java.util.Set;

/**
 * xrpc 框架专用TCP协议
 *
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 15:53
 */
public abstract class XrpcProtocol {

    public static XrpcProtocol DEFAULT_PROTOCOL() {
        return new XrpcProtocolV1();
    }

    /**
     * 获取魔数
     */
    public abstract int getMagicNum();

    /**
     * 获取版本
     */
    public abstract int getVersion();

    /**
     * 获取序列化处理器
     */
    public abstract int getSerializer();

    public abstract boolean isSupport(int magicNum, int version, Integer serialize, Integer clazz);


}
