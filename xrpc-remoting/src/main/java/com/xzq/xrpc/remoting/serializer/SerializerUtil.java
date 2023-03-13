package com.xzq.xrpc.remoting.serializer;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 15:48
 */
public class SerializerUtil {

    private static Serializer JSON = new JSONSerializer();
    private static Serializer JDK = new JDKSerializer();
    private static Serializer PROTOBUF = new ProtoBufSerializer();


    public static Serializer JSON() {
        return JSON;
    }
    public static Serializer JDK() {
        return JDK;
    }
    public static Serializer PROTOBUF() {
        return PROTOBUF;
    }

    public static Serializer match(int type) {
        switch (type) {
            case Serializer.JDK:
                return JDK;
            case Serializer.JSON:
                return JSON;
            case Serializer.PROTOBUF:
                return PROTOBUF;
            default:
                return null;
        }
    }


}
