package com.xzq.xrpc.remoting.message;

import com.xzq.xrpc.remoting.config.MessageCodecConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * xrpc请求消息体
 * @Author xzq
 * @Version 1.0.0
 * @Date 2023/3/9 17:33
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XrpcRequestMessage extends Message implements Serializable {

    /**
     * 接口名称
     */
    private String interfaceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class[] parameterTypes;
    /**
     * 参数实际值
     */
    private Object[] parameterValues;

    /**
     * 方法返回类型
     */
    private Class<?> returnType;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }

    @Override
    public int getMessageType() {
        return Message.XrpcRequestMessage;
    }

}
