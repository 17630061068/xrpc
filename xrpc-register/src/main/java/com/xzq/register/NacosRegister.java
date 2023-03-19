package com.xzq.register;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.xzq.register.model.ProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/19 16:12
 */
public class NacosRegister implements Register{

    private Logger logger = LoggerFactory.getLogger(NacosRegister.class);

    private NamingService namingService;

    public NacosRegister() {
    }

    public NacosRegister(NamingService namingService) {
        this.namingService = namingService;
    }

    @Override
    public void init() {

    }

    @Override
    public void register(ProviderService providerServer) {
        try {
            namingService.registerInstance(providerServer.getServiceName(), providerServer.getHost(), providerServer.getPort());
        } catch (NacosException e) {
            logger.error("Xrpc nacos 服务注册失败，providerService:{}", JSONObject.toJSONString(providerServer));
        }
    }

    @Override
    public void register(List<ProviderService> providerServer) {
        providerServer.forEach(this::register);
    }

    @Override
    public List<ProviderService> findServices(String serviceName) {

        try {
            List<Instance> instances = namingService.selectInstances(serviceName, Boolean.TRUE);

            return instances.stream().map(obj -> {
                ProviderService providerService = new ProviderService();
                providerService.setServiceName(obj.getServiceName());
                providerService.setHost(obj.getIp());
                providerService.setPort(obj.getPort());
                return providerService;
            }).collect(Collectors.toList());

        } catch (NacosException e) {
            logger.error("Xrpc 拉取服务失败");
            throw new RuntimeException(e);
        }
    }
}
