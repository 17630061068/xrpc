package com.xzq.xrpc.remoting.serializer;

import java.io.*;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 15:11
 */
public class JDKSerializer implements Serializer{

    @Override
    public <T> byte[] serializer(T object) {

        try {
            ByteArrayOutputStream aos = new ByteArrayOutputStream();

            ObjectOutputStream oos = new ObjectOutputStream(aos);

            oos.writeObject(object);

            return aos.toByteArray();

        } catch (IOException e) {

            e.printStackTrace();

            throw new RuntimeException("jdk序列化失败");

        }

    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> clazz) {

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {

           return  (T) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

            throw new RuntimeException("jdk反序列化失败");

        }
    }

}
