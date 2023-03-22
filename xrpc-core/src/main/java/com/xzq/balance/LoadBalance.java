package com.xzq.balance;

import java.util.List;

/**
 * @desc 负载均衡顶层接口
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/22 18:41
 */

public interface LoadBalance {

    <T >T loadBalance(String targetName,List<T> targetGroup);
}
