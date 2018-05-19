# Spring Cloud Demo
## 服务注册中心与服务提供者(Eureka Server & Client)

1. 版本序列Release Trains：为了方便管理Spring Could中各种各样的依赖，制定了
依赖集合，即版本序列。版本序列主要包含Spring Boot和Spring Cloud的版本，具体参考
http://projects.spring.io/spring-cloud/
2. cannot resolve symbol SpringBootApplication：找不到
Maven: org.springframework.cloud:spring-cloud-netflix-eureka-server，我认为是因为
Spring Cloud的Dalston.SR5版本序列里找不到spring-cloud-netflix-eureka-server，所以
我手动添加的<version>1.4.4.RELEASE</version>

### Eureka Server服务注册中心

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
### Eureka Client服务提供者

1. File->New->Project->Spring Initializer->Cloud Discovery->Eureka Server
2. @EnableEurekaClient注解表示当前服务是一个服务提供者
3. 警告信息：
    ```
    EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
    ```
    - 本机测试时，server收到的心跳次数不足，认为服务挂掉了。
    - 解决方法：  server.renewal-percent-threshold: 0.49
4. 测试地址：http://localhost:8762/hi?name=David


-------------
## 其他
### fatal: refusing to merge unrelated histories
合并报错
```
git merge origin/master --allow-unrelated-histories
```

