package com.xzq.util;

import java.util.Collection;
import java.util.List;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 13:54
 */
public class ListUtil {

    public static  <T> List<T> toList(Collection<T> collection) {
        return cn.hutool.core.collection.ListUtil.toList(collection);
    }

    public static  <T> List<T> toList(T collection) {
        return cn.hutool.core.collection.ListUtil.toList(collection);
    }
}
