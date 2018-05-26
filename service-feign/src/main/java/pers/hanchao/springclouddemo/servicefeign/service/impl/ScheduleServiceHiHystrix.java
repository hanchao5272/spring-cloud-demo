package pers.hanchao.springclouddemo.servicefeign.service.impl;

import org.springframework.stereotype.Component;
import pers.hanchao.springclouddemo.servicefeign.service.ScheduleServiceHi;

/**
 * <p>service-hi的熔断方法</p>
 * @author hanchao 2018/5/19 17:56
 **/
@Component
public class ScheduleServiceHiHystrix implements ScheduleServiceHi {
    /**
     * 熔断方法，返回提示信息
     * @param name
     * @return
     */
    @Override
    public String sayHiFromServiceHi(String name) {
        return "Sorry " + name + ",service-hi is not available.";
    }
}
