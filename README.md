# Spring Cloud Demo
[TOC]
## 服务注册中心与服务提供者(Eureka Server & Client)
Eureka  [juəˈri:kə] 我发现了,服务注册中心,服务提供者

1. 版本序列Release Trains：为了方便管理Spring Could中各种各样的依赖，制定了
依赖集合，即版本序列。版本序列主要包含Spring Boot和Spring Cloud的版本，具体参考
http://projects.spring.io/spring-cloud/
2. cannot resolve symbol SpringBootApplication：找不到
Maven: org.springframework.cloud:spring-cloud-netflix-eureka-server，我认为是因为
Spring Cloud的Dalston.SR5版本序列里找不到spring-cloud-netflix-eureka-server，所以
我手动添加的<version>1.4.4.RELEASE</version>

### Eureka Server服务注册中心(eureka-server)

1. File->New->Project->Spring Initializer->Cloud Discovery->Eureka Server
2. @EnableEurekaServer注解使其成为服务注册中心Eureka Server
3. application.yml配置
    ```yml
    #服务
    server:
      #端口号
      port: 8761

    #服务注册配置
    eureka:
      #实例
      instance:
        #主机域名
        hostname: localhost
      #Eureka Server也是一个Eureka Client，需要制定Server
      client:
        #是否通过eureka注册
        register-with-eureka: false
        #是否获取注册
        fetch-registry: false
        #注册中心地址
        service-url:
          default-zone: http://${eureka.instance.hostname}:${server.port}/eureka/
    ```
4. Eureka Server 地址：http://localhost:8761/
### Eureka Client服务提供者(eureka-hi)

1. File->New->Project->Spring Initializer->Cloud Discovery->Eureka Server
2. @EnableEurekaClient注解表示当前服务是一个服务提供者
3. 警告信息：
    ```
    EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
    ```
    - 本机测试时，server收到的心跳次数不足，认为服务挂掉了。
    - 解决方法：  server.renewal-percent-threshold: 0.49
4. 测试地址：http://localhost:8762/hi?name=David

## 服务消费者(Feign)
Feign [feɪn] 伪装,伪Http客户端,负载均衡,可插拔注解

1. 依赖：Web->Web、Cloud Routing->Eureka Server、Cloud Routing->Feign
2. @EnableFeignClients注解表示当前服务开启了Feign功能
3. @EnableDiscoveryClient表示当前服务是一个消息提供者，比@EnableEurekaClient
更通用，可以适用于其他服务注册中心；@EnableEurekaClient只适用于Eureka注册中心
4. Feign自动做了负载均衡

## 断路器(Hystrix)
Hystrix [hɪst'rɪks]  豪猪,防御机制,防止雪崩,降级,当服务不可用时的默认方法

1. Feign是自带断路器Hystrix的，配置开启
    ```
    feign:
      hystrix:
        enabled: true
    ```
2. 定义Feign客户端接口ScheduleServiceHi的实现类，在其中编写熔断方法
3. 在Feign客户端接口ScheduleServiceHi接口中，指定熔断时fallback(后退)的类

## 路由网关(Zuul)
Zuul 负载均衡,路由转发,过滤器

1. 依赖：Web->Web、Cloud Routing->Eureka Server、Cloud Routing->Zuul
2. @EnableEurekaClient注解开启服务注册
3. @EnableZuulProxy注解开启网关代理
3. @需要在application.yml中设置失效时间，以解决Zuul导致Hystrix熔断失效的问题


-------------
## 其他
### fatal: refusing to merge unrelated histories
合并报错
```
git merge origin/master --allow-unrelated-histories
```

