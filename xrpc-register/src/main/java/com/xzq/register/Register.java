package com.xzq.register;

import com.xzq.register.model.ProviderService;

import java.util.List;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 18:52
 */
public interface Register {

     void init();

    /**
     * 注册服务
     * @param providerServer
     */
    void register(ProviderService providerServer);

    /**
     * 注册服务
     * @param providerServer
     */
    void register(List<ProviderService> providerServer);

    /**
     * 发现服务
     * @param serviceName
     * @return
     */
    List<ProviderService> findServices(String serviceName);
}
