package com.xzq.util;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 13:39
 */
public class ObjectUtil {

    public static Boolean isNull(Object o) {
        return cn.hutool.core.util.ObjectUtil.isNull(o);
    }

    public static Boolean notNull(Object o) {
        return cn.hutool.core.util.ObjectUtil.isNotNull(o);
    }
}
