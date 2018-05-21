package pers.hanchao.springclouddemo.zipkinhi.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by 韩超 on 2018/5/21.
 */
@FeignClient(value = "zipkin-ping")
public interface ServicePing {

    /**
     * 调用zipkin-ping的ping方法
     * @return
     */
    @GetMapping("/ping")
    public String pingFromZipkinPing();

}
