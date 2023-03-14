package com.xzq.xrpc.remoting.message;

/**
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:37
 */
public class XrpcResponseMessage extends Message {

    /**
     * 返回值
     */
    private Object returnValue;

    /**
     * 异常值
     */
    private Throwable throwable;

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }


    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public int getMessageType() {
        return Message.XrpcResponseMessage;
    }


}
