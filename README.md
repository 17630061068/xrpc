[![stars](https://badgen.net/github/stars/17630061068/xrpc?icon=github&color=4ab8a1)](https://github.com/17630061068/xrpc) 
[![forks](https://badgen.net/github/forks/17630061068/xrpc?icon=github&color=4ab8a1)](https://github.com/17630061068/xrpc) 

### 🚀xrpc
open source RPC framework

#### 模块描述
- [xrpc-bootstrap](xrpc-bootstrap) : xrpc启动器
- [xrpc-client](xrpc-client) ： xrpc消费者模块
- [xrpc-core](xrpc-core) : xrpc核心模块
- [xrpc-register](xrpc-register)： xrpc注册中心模块
- [xrpc-remoting](xrpc-remoting): xrpc远程处理模块
- [xrpc-server](xrpc-server)： xrpc提供者模块
- [xrpc-spring-boot-starter](xrpc-spring-boot-starter) ： xrpc整合springBoot
- [xrpc-util](xrpc-util): xrpc工具类模块

### 📑 目前进度

1. 完成了基本的远程调用功能
2. 完成了简易版IOC和DI功能
3. 可以独立运行
4. 和springBoot进行了整合
5. 注册中心整合redis+nacos
6. client和server 使用ping-pong机制+netty[IdleHandler]完成心跳检测功能
7. 支持jdk,json,protobuf等序列化机制
8. 定义了Xrpc协议报文,使用LTC（Length-Type-Content）机制 解决TCP半包粘包
9. Xrpc协议由11个字节组成协议头，4字节魔数，1字节版本号，1字节序列化方案，1字节消息指令类型，4字节长度

### 🚗 待优化功能

1. xrpc-client  一个远程Client对象应该与Channel对象进行绑定，而不是直接和NettyBootStrap进行绑定
2. 项目中没有进行合适的设计和面向对象设计（需要优化，比如服务暴露的流程）
3. xrpc-core 完成IOC功能，解决循环依赖问题

### 🚕 待开发功能
1. 心跳检测 + 断线重连  done 开发完毕
2. 负载均衡   
3. 超时熔断   

### 🎯 下一阶段开发工作 (1.0.1-SNAPSHOT)
1. 解决提供者调用异常时，消费者的异常打印问题
2. 完善XrpcClient,和XrpcServer职责划分（抽象其功能)
3. 完善Xrpc-BootStrap 
4. 增加负载均衡功能,和超时熔断功能


### 使用说明
[Wiki](https://github.com/17630061068/xrpc/wiki)

