package com.xzq.ioc.context;

import com.xzq.ioc.annotation.Service;
import com.xzq.test.TestService;
import com.xzq.test.TestService2;
import com.xzq.util.IocUtil;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ClassPathApplicationContextTest {

    @Test
    public void test_ioc() {
        ClassPathApplicationContext applicationContext = new ClassPathApplicationContext("com.xzq");

//        TestService testService =(TestService) applicationContext.getBean("testService");
//
//        testService.getTestService2().test();
//        System.out.println(testService);

        List<Object> list = applicationContext.getBeansWithAnnotation(Service.class);

        Object testService = list.get(0);

        System.out.println(testService);

    }

}