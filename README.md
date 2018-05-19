# Spring Cloud Demo

1. 版本序列Release Trains：为了方便管理Spring Could中各种各样的依赖，制定了
依赖集合，即版本序列。
2. 版本序列主要包含Spring Boot和Spring Cloud的版本，具体参考http://projects.spring.io/spring-cloud/
3. 我的版本号：Spring Cloud:Dalston.SR5,Spring Boot:1.5.13.RELEASE
4. cannot resolve symbol SpringBootApplication：找不到jar包，造成原因：版本序列不匹配
如果Spring Cloud:Dalston.SR5,Spring Boot:1.5.13.RELEASE，则不要使用各组件的最新版本。
如Eureka要使用spring-cloud-starter-eureka-server，而不是spring-cloud-starter-netflix-eureka-client
5. 创建项目顺序：
    1. 创建一个Maven项目(类型无所谓，删除src目录)
    2. 右键项目->New->Module->Spring Initializer->选择组件->...

[TOC]
## 服务注册中心与服务提供者(Eureka Server & Client)
Eureka  [juəˈri:kə] 我发现了,服务注册中心,服务提供者

### Eureka Server服务注册中心(eureka-server)

1. 组件：web、eureka
2. @EnableEurekaServer注解使其成为服务注册中心Eureka Server
3. application.yml配置(不能有中文，这里是注释)
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

1. 组件：web、eureka
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

1. 组件：web、eureka、feign
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

### 路由转发(Routing)
1. 组件：web、eureka、zuul
2. @EnableEurekaClient注解开启服务注册
3. @EnableZuulProxy注解开启网关代理
4. @需要在application.yml中设置失效时间，以解决Zuul导致Hystrix熔断失效的问题
    ```
    zuul:
      socket-timeout-millis: 60000
      connect-timeout-millis: 60000
      routes:
        api-a:
          path: /api-a/**
          service-id: service-feign
    ribbon:
      ReadTimeout: 60000
      ConnectTimeout: 60000
    hystrix:
      command:
        default:
          execution:
            isolation:
              thread:
                timeoutInMilliseconds: 60000
    ```

### 过滤器(filter)

1. 自定义过滤器继承自ZuulFilter，需重写filterType、filterOrder、shouldFilter和run方法，注解@Componet
2. filterType表示过滤器类型，pre(路由转发之前),routing(路由之时),post(路由之后),error(发生出错之时)
3. filterOrder表示过滤器顺序，shouldFilter表示过滤规则，run表示过滤逻辑

-------------
## 其他
### fatal: refusing to merge unrelated histories
合并报错
```
git merge origin/master --allow-unrelated-histories
```

