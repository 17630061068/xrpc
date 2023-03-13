package com.xzq.util;

import java.util.Collection;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 19:24
 */
public class CollectionUtil {

    public static Boolean isEmpty(Collection collection) {
        return cn.hutool.core.collection.CollectionUtil.isEmpty(collection);
    }

    public static Boolean notEmpty(Collection collection) {
        return cn.hutool.core.collection.CollectionUtil.isNotEmpty(collection);
    }
}
