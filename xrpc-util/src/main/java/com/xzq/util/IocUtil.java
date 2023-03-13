package com.xzq.util;

import cn.hutool.core.util.StrUtil;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 10:30
 */
public class IocUtil {

    public static String getDefaultBeanName(Class clazz) {
        String simpleName = clazz.getSimpleName();
        return StrUtil.lowerFirst(simpleName);
    }
}
