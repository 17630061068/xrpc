package com.xzq.ioc;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/10 10:20
 */
public class BeansException extends RuntimeException{
    public BeansException() {
        super();
    }

    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
