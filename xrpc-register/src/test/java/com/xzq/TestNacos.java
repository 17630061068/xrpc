package com.xzq;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Properties;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/14 23:26
 */
public class TestNacos {
    public static void main(String[] args) {
        try {

            Properties properties = new Properties();
            properties.setProperty("serverAddr", "175.24.165.137:8848");
            properties.setProperty("namespace", "81d67530-c06d-451b-b295-51d1b121cf78");
//            properties.setProperty("username", "nacos");
//            properties.setProperty("password", "2535b7a8991017d5");
            NamingService namingService = NacosFactory.createNamingService(properties);

            namingService.registerInstance("test-service","127.0.0.1",20001);

            List<Instance> instances = namingService.getAllInstances("test-service");
            instances.forEach(obj->{
                System.out.println(obj.getServiceName());
                System.out.println(obj.getPort());
                System.out.println(obj.getIp());
            });
            Thread.sleep(10000);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
