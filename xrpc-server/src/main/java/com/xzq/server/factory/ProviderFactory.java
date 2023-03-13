package com.xzq.server.factory;

/**
 *  提供者工厂
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 18:04
 */
public interface ProviderFactory {

    Object getProvider(String interfaceName);


}
