package com.xzq.util;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/22 18:45
 */
public class RandomUtil {

    public static int randomInt(Integer max, Integer min) {
        return cn.hutool.core.util.RandomUtil.randomInt(min, max);
    }

}
