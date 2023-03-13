package com.xzq.util;

import java.util.Set;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 09:44
 */
public class ClassUtil {

    public static Set<Class<?>> scanPackage(String classPath) {
        return cn.hutool.core.util.ClassUtil.scanPackage(classPath);
    }
}
