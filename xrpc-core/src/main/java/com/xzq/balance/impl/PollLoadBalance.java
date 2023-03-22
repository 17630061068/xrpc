package com.xzq.balance.impl;

import com.xzq.balance.LoadBalance;
import com.xzq.util.ObjectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @desc 轮询负载均衡器
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/22 18:43
 */
public class PollLoadBalance implements LoadBalance {

    private Map<String, AtomicInteger> counterMap = new HashMap<>();


    @Override
    public <T> T loadBalance(String targetName, List<T> targetGroup) {

        AtomicInteger atomicInteger = counterMap.get(targetName);

        if (ObjectUtil.isNull(atomicInteger)) {

            AtomicInteger counter = new AtomicInteger(0);

            counterMap.put(targetName, counter);

            return targetGroup.get(counter.getAndIncrement() % targetGroup.size());
        }else{

            int index = atomicInteger.getAndIncrement() % targetGroup.size();

            return targetGroup.get(index);
        }
    }
}
