package com.xzq.register;

import com.alibaba.fastjson2.JSONObject;
import com.xzq.register.config.RegisterConfig;
import com.xzq.register.model.ProviderService;
import redis.clients.jedis.Jedis;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 18:54
 */
public class RedisRegister implements Register {
    private static final String PREFIX = "RPC:SERVERS:";
    private static final List<String> registerServices = new ArrayList<>();
    private String host;
    private Integer port;
    private String auth;

    private Boolean isInit = Boolean.FALSE;

    private Jedis jedis;

    public RedisRegister() {

    }

    public RedisRegister(RegisterConfig registerConfig) {
        this.host = registerConfig.getHost();
        this.port = registerConfig.getPort();
        this.auth = registerConfig.getAuth();
        init();
    }

    @Override
    public synchronized void init() {
        if (!isInit) {
            jedis = new Jedis(host, port);
            if (auth != null && !"".equals(auth)) {
                jedis.auth(auth);
            }
            isInit = Boolean.TRUE;
        }
    }

    @Override
    public void register(ProviderService providerServer) {
        String key = PREFIX + providerServer.getServiceName();
        registerServices.add(key);
        jedis.sadd(key, JSONObject.toJSONString(providerServer));
    }

    @Override
    public void register(List<ProviderService> providerServer) {
        providerServer.forEach(this::register);
    }

    @Override
    public List<ProviderService> findServices(String serviceName) {
        Set<String> services = jedis.smembers(PREFIX + serviceName);

        List<ProviderService> serviceList = services.stream().map(body -> {
            return JSONObject.parseObject(body, ProviderService.class);
        }).collect(Collectors.toList());

        return serviceList;
    }
}
