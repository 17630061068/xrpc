package com.xzq.balance.impl;

import com.xzq.balance.LoadBalance;
import com.xzq.util.RandomUtil;

import java.util.List;

/**
 * @desc 随机负载均衡器
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/22 18:43
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public <T> T loadBalance(String targetName, List<T> targetGroup) {

        int size = targetGroup.size();

        int index = RandomUtil.randomInt(size, 0);

        return targetGroup.get(index);
    }
}
