package pers.hanchao.springclouddemo.servicefeign.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>一个Feign服务消费者接口</p>
 * @author hanchao 2018/5/19 15:57
 **/
@FeignClient(value = "service-hi")
public interface ScheduleServiceHi {
    @GetMapping("/hi")
    String sayHiFromServiceHi(@RequestParam(value = "name") String name);
}
