package com.xzq.xrpc.remoting.serializer;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 15:33
 */
public class ProtoBufSerializer implements Serializer{

    /**
     * 缓存scheme
     * @param object
     * @return
     * @param <T>
     */
    private Map<Class<?>, Schema> schemaMap = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd();

    @Override
    public <T> byte[] serializer(T object) {
        try {
            LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

            Class<T> clazz = (Class<T>) object.getClass();

            Schema<T> schema = getSchema(clazz);

            byte[] bytes = ProtostuffIOUtil.toByteArray(object, schema, buffer);

            return bytes;
        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("protobuf序列化失败");

        }
    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> clazz) {

        try {

            T message = objenesis.newInstance(clazz);

            Schema<T> schema = getSchema(clazz);

            ProtostuffIOUtil.mergeFrom(bytes, message, schema);

            return message;
        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException("protobuf反序列化失败");

        }

    }

    private <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = schemaMap.get(clazz);
        if (schema == null) {
            schema= RuntimeSchema.createFrom(clazz);
            schemaMap.put(clazz, schema);
        }
        return schema;
    }
}
