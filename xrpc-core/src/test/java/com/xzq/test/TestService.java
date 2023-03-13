package com.xzq.test;

import com.xzq.ioc.annotation.Autowired;
import com.xzq.ioc.annotation.Service;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/13 10:28
 */
@Service
public class TestService {

    @Autowired
    private TestService2 testService2;

    public TestService2 getTestService2() {
        return testService2;
    }

    public void setTestService2(TestService2 testService2) {
        this.testService2 = testService2;
    }
}
