package com.xzq.xrpc.remoting.serializer;

import com.alibaba.fastjson2.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 15:19
 */
public class JSONSerializer implements Serializer {

    @Override
    public <T> byte[] serializer(T object) {

        String body = JSONObject.toJSONString(object);

        return body.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserializer(byte[] bytes, Class<T> clazz) {

        String body = new String(bytes, StandardCharsets.UTF_8);

        return JSONObject.parseObject(body, clazz);

    }

}
