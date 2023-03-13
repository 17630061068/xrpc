package com.xzq.ioc.annotation;

import java.lang.annotation.*;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 09:58
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Autowired {

}
