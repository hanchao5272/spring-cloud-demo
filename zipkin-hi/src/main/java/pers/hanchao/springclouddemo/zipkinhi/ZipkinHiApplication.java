package pers.hanchao.springclouddemo.zipkinhi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pers.hanchao.springclouddemo.zipkinhi.service.ServicePing;
import pers.hanchao.springclouddemo.zipkinhi.service.ServicePong;

@EnableEurekaClient
@RestController
@SpringBootApplication
@EnableFeignClients
public class ZipkinHiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinHiApplication.class, args);
	}


	@Autowired
	private ServicePing servicePing;

	@Autowired
	private ServicePong servicePong;

	/**
	 * 调用多次ping-pong
	 * @param count
	 * @return
	 */
	@GetMapping("/hi/{count}")
	public String hi(@PathVariable("count") Integer count) throws InterruptedException {
		if (count < 0 || count > 3){
			count = 3;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < count; i++) {
			String ping = servicePing.pingFromZipkinPing();
			String pong = servicePong.pongFromZipkinPong();
			Long time = System.currentTimeMillis();
		    stringBuilder.append("[").append(ping).append("-->").append(pong).append(",time:").append(time).append("]\n");
		}
		return stringBuilder.toString();
	}
}
