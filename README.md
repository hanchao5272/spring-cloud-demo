# Spring Cloud Demo

1. 版本序列Release Trains：为了方便管理Spring Could中各种各样的依赖，制定了
依赖集合，即版本序列。
2. 版本序列主要包含Spring Boot和Spring Cloud的版本，具体参考http://projects.spring.io/spring-cloud/
3. 我的版本号：Spring Cloud:Dalston.RELEASE,Spring Boot:1.5.2.RELEASE
4. cannot resolve symbol SpringBootApplication：找不到jar包，造成原因：版本序列不匹配
如果Spring Cloud:Dalston.RELEASE,Spring Boot:1.5.2.RELEASE，则不要使用各组件的最新版本。
如Eureka要使用spring-cloud-starter-eureka-server，而不是spring-cloud-starter-netflix-eureka-client
5. 创建项目顺序：
    1. 创建一个Maven项目(类型无所谓，删除src目录)
    2. 右键项目->New->Module->Spring Initializer->选择组件->...

[TOC]


## 1.服务注册中心与服务提供者(Eureka)
Eureka  [juəˈri:kə] 我发现了,服务注册中心,服务提供者

### 1.1.Eureka Server服务注册中心(eureka-server)

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

### 1.2.Eureka Server的高可用配置

1. 在yml文件中用---来区分多个文件，减少配置文件个数
2. 多个实例共用一个服务名：spring.application.name=eureka-ha
3. 每个实例都需要注册到其他的注册中心：
    ```
     server:
       port: 8771
     spring:
       profiles: peer1
       application:
         name: eureka-ha
     eureka:
       instance:
         hostname: peer1
       client:
         serviceUrl:
           defaultZone: http://peer2:8772/eureka/,http://peer3:8773/eureka/
    ```
4. 在IDEA中，分别通过设置不同的Program arguments参数为--spring.profiles.active=peer1、
--spring.profiles.active=peer2、--spring.profiles.active=peer3启动这三个注册中心服务。
5. Eureka Server与Eureka Client的配置文件全都采用驼峰命名

### 1.3.Eureka Server的安全认证

1. 在Eureka Server中添加依赖spring-boot-starter-security
2. 修改Eureka Server的配置，添加认证配置security
```
security:
  basic:
    enabled: true
  user:
    name: admin
    password: admin
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${security.user.name}:${security.user.password}@${eureka.instance.hostname}:${server.port}/eureka/
```
3. 修改Eureka Client配置，修改defaultZone=http://usr:pwd@host:ip/eureka/
4. Eureka Server与Eureka Client的配置文件全都采用驼峰命名

### 1.4.Eureka Client服务提供者(eureka-hi)

1. 组件：web、eureka
2. @EnableEurekaClient注解表示当前服务是一个服务提供者
3. 警告信息：
    ```
    EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
    ```
    - 本机测试时，server收到的心跳次数不足，认为服务挂掉了。
    - 解决方法：  server.renewal-percent-threshold: 0.49
4. 测试地址：http://localhost:8762/hi?name=David

## 2.服务消费者(Feign,service-feign)
Feign [feɪn] 伪装,伪Http客户端,负载均衡,可插拔注解

1. 组件：web、eureka、feign
2. @EnableFeignClients注解表示当前服务开启了Feign功能
3. @EnableDiscoveryClient表示当前服务是一个消息提供者，比@EnableEurekaClient
更通用，可以适用于其他服务注册中心；@EnableEurekaClient只适用于Eureka注册中心
4. Feign自动做了负载均衡

## 3.断路器(Hystrix,service-feign)
Hystrix [hɪst'rɪks]  豪猪,防御机制,防止雪崩,降级,当服务不可用时的默认方法

### 3.1.断路器(Hystrix,service-feign)

1. Feign是自带断路器Hystrix的，配置开启
    ```
    feign:
      hystrix:
        enabled: true
    ```
2. 定义Feign客户端接口ScheduleServiceHi的实现类，在其中编写熔断方法
3. 在Feign客户端接口ScheduleServiceHi接口中，指定熔断时fallback(后退)的类

### 3.2.断路器仪表盘(Hystrix Dashboard,service-feign)

1. jar包引入：hystrix、hystrix-dashboard
2. 在启动类添加@EnableHystrix、@EnableHystrixDashboard
3. 通过http://localhost:8765/hystrix.stream 进行服务断路情况流式监控
4. 通过http://localhost:8765/hystrix 进行服务断路情况仪表盘监控
5. 仪表盘参数：http://localhost:8765/hystrix.stream、2000、SERVICE-FEIGN
6. 修改application.yml,添加注册认证，并修改配置为驼峰方式

### 3.3.断路器仪表盘聚合(Turbine,service-feign,service-feign-2,service-turbine)
[ˈtɜ:baɪn] 涡轮机;将若干服务的监测数据聚合起来

1. 注意主类的注解顺序
2. 引入jar包：starter-turbine、netflix-turbine
3. 引入注解：@EnableEurekaClient、@EnableTurbine
4. application.yml配置：
```
turbine:
  aggregator:
    clusterConfig: default   # 指定聚合哪些集群，多个使用","分割，默认为default。可使用http://.../turbine.stream?cluster={clusterConfig之一}访问
  appConfig: service-feign,service-feign-2  # 配置Eureka中的serviceId列表，表明监控哪些服务
  clusterNameExpression: new String("default")
  # 1. clusterNameExpression指定集群名称，默认表达式appName；此时：turbine.aggregator.clusterConfig需要配置想要监控的应用名称
  # 2. 当clusterNameExpression: default时，turbine.aggregator.clusterConfig可以不写，因为默认就是default
  # 3. 当clusterNameExpression: metadata['cluster']时，假设想要监控的应用配置了eureka.instance.metadata-map.cluster: ABC，
  #    则需要配置，同时turbine.aggregator.clusterConfig: ABC
```
5. 驼峰配置
6. 流访问：http://localhost:8969/turbine.stream
7. turbine仪表盘访问http://localhost:8865/hystrix ，录入http://localhost:8969/turbine.stream、
2000、[随便]，点击Monitor Stream

## 4.路由网关(Zuul)
Zuul 负载均衡,路由转发,过滤器

### 4.1.路由转发(Routing,service-zuul)
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

### 4.2.过滤器(ZuuFilter,service-zuul)

1. 自定义过滤器继承自ZuulFilter，需重写filterType、filterOrder、shouldFilter和run方法，注解@Componet
2. filterType表示过滤器类型，pre(路由转发之前),routing(路由之时),post(路由之后),error(发生出错之时)
3. filterOrder表示过滤器顺序，shouldFilter表示过滤规则，run表示过滤逻辑

## 5.配置中心与获取配置(Config)
Config,配置

### 5.1.基础配置与手动refresh
#### 5.1.配置中心(Config Server,config-server)

1. Config Server相关jar包是spring-cloud-config-server
2. 注解@EnableConfigServer表示此服务是配置中心
3. 注解@EnableEurekaClient进行服务注册
4. 配置说明(这里的注释只是说明，实际不能用中文)
    ```
    spring:
      cloud:
        config:
          server:
            git:
              # 配置文件所在github项目路径
              uri: https://github.com/hanchao5272/spring-cloud-demo/
              # 配置文件所在目录路径
              search-paths: config-repo
              # username/password
          # 配置文件所在github项目的分支
          label: master
    ```
4. 读取配置文件的路径规则
    - /{application}/{profile}[/{label}]
    - /{application}-{profile}.yml
    - /{label}/{application}-{profile}.yml
    - /{application}-{profile}.properties
    - /{label}/{application}-{profile}.properties
5. 读取配置文件举例：配置文件名为config-client-dev.yml，则application=config-client，
profile=dev，label=master。最终读取URL：http://localhost:8888/config-client/dev

#### 5.2.配置客户端(Config Client,config-client)

1. Config Client相关jar包是spring-cloud-starter-config
2. 注解@EnableEurekaClient进行服务注册
3. bootstrap.yml要早于application.yml加载，所以将config相关配置写在bootstrap.yml
4. 配置说明
    ```
    spring:
      cloud:
        config:
          # 配置文件所在github项目的分支
          label: master
          # 配置文件环境：dev(开发)/test(测试)/pro(生产),，对应{profile}
          profile: dev
          # 配置中心服务地址
          uri: http://localhost:8888/
          # 配置名，对应{application}
          name: config-client
    ```
4. Config-Client读取Config-Server的配置，而Config-Server的配置来自于github
5. 要开启手动刷新，需要引入jar包：actuator  ['æktʃʊeɪtə]  执行者
6. 要开启手动刷新，需要在引用配置的相关类上，添加注解@@RefreshScope

### 5.2.高可用配置与手动refresh

1. Config Server、Config Client都需要通过@EnableEurekaClient注册服务
2. Config Server通过修改端口号开启多个服务，形成服务集群
3. Config Client修改spring.cloud.config的配置方式
    ```
    spring:
      cloud:
        config:
          label: master
          profile: dev
          uri: http://localhost:8888/
    ============>>>>>>>>>==================
    spring:
      cloud:
        config:
          label: master
          profile: dev
          discovery:
            enabled: true
            serviceId: config-server
    ```
4. 刷新时无需认证：management.security.enabled=false
5. 注意将配置写在bootstrap.yml，优先加载

### 5.3.自动刷新/bus/refresh

#### 5.3.1.初始版本：客户端刷新

1. Config Client添加bus-amqp依赖
2. Config Client添加rabbitmq相关配置
```
  # rabbitmq相关配置
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```
3. 刷新时，随意对某个Client的服务进行POST操作：/bus/refresh，则其他Client同时被刷新
4. 缺陷：未被微服务职责单一性，Client应该承担业务，而非配置刷新工作
5. 缺陷：破坏了个微服务的对等性，被刷新的微服务是特殊的
6. 缺陷：有一定局限性，如果被刷新的微服务发生变化，则需要另行配置
7. 改进：对Config Server实施刷新

#### 5.3.2.改进版本：服务端刷新

1. 在上个版本的基础上，对Config Server进行同样的配置
2. 刷新时，对Config Server进行POST：/bus/refresh
3. 此种版本下，Client无需配置Rabbitmq相关信息，因为无需向rabbitmq发送消息，但是需要有bus-amqp依赖

#### 5.3.3.其他

1. 刷新时，关闭安全验证：management.security.enabled=false
2. 开启消息跟踪spring.cloud.bus.trace.enabled=true,可以
通过[GET]http://localhost:8887/trace 查看刷新记录
3. 局部刷新：/bus/refresh?destination=customers:8000，其中，
customers:8000指的是各个微服务的ApplicationContext ID。

## 6.服务链路追踪(Sleuth)
sleuth [slu:θ] 侦探,分析服务间的调用关系

### 6.1.HTTP模式

#### 6.1.1.Sleuth Server(zipkin-server)

1. Sleuth Server依赖：zipkin-server/zipkin-autoconfigure-ui
2. @EnableZipkinServer注解表示该服务作为链路追踪中心
3. @EnableEurekaClient注解表示该服务进行注册

#### 6.1.2.Sleuth Client(zipkin-hi/zipkin-ping/zipkin-pong)

1. Sleuth Client依赖：spring-cloud-starter-zipkin/zipkin-server/zipkin-autoconfigure-ui
2. @EnableEurekaClient注解表示该服务进行注册
3. zipkin client配置
    ```yml
    spring
      #Zipkin Server服务地址
      zipkin:
        base-url: http://localhost:9411
      #采样比例(默认0.1)
      sleuth:
        sampler:
          percentage: 1
    ```
#### 6.1.3.缺陷

1. Zipkin Client向Zipkin-Server程序发送数据使用的是http的方式通信，每次发送的时候涉及到连接和发送过程，对原有业务接口调用产生较大影响。
2. 当我们的zipkin-server程序关闭或者重启过程中，因为客户端收集信息的发送采用http的方式会被丢失。




---
## 其他
### fatal: refusing to merge unrelated histories
合并报错
```
git merge origin/master --allow-unrelated-histories
```

