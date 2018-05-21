package pers.hanchao.springclouddemo.zipkinhi.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by 韩超 on 2018/5/21.
 */
@FeignClient(value = "zipkin-pong")
public interface ServicePong {

    /**
     * 调用zipkin-pong服务的pong方法
     * @return
     */
    @GetMapping("/pong")
    public String pongFromZipkinPong();
}
