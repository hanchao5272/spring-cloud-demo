package pers.hanchao.springclouddemo.servicefeign2.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pers.hanchao.springclouddemo.servicefeign2.service.impl.ScheduleService2HiHystrix;

/**
 * <p>一个Feign服务消费者接口</p>
 * @author hanchao 2018/5/19 15:57
 **/
@FeignClient(value = "service-hi",fallback = ScheduleService2HiHystrix.class)
public interface ScheduleService2Hi {
    /**
     * <p>通过Feign伪Http客户端调用service-hi提供的服务，并定义熔断方法</p>
     * @author hanchao 2018/5/19 17:59
     **/
    @GetMapping("/hi")
    String sayHiFromServiceHi(@RequestParam(value = "name") String name);
}
