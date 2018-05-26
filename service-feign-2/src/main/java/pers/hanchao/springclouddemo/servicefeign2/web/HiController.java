package pers.hanchao.springclouddemo.servicefeign2.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pers.hanchao.springclouddemo.servicefeign2.service.ScheduleService2Hi;

/**
 * <p>服务消费控制层</p>
 * @author hanchao 2018/5/19 15:59
 **/
@RestController
public class HiController {
    /** 注入服务"service-hi"的Feign客户端ScheduleServiceHi */
    @Autowired
    private ScheduleService2Hi scheduleService2Hi;

    /**
     * 调用Feign客户端提供的服务，自带负载均衡
     * @param name
     * @return
     */
    @GetMapping("/hi")
    public String sayHi(@RequestParam String name){
        //调用Feign客户端ScheduleServiceHi的接口
        return scheduleService2Hi.sayHiFromServiceHi(name);
    }
}
