package pers.hanchao.springclouddemo.eurekahiha;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@RestController
@SpringBootApplication
public class EurekaHiHaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaHiHaApplication.class, args);
	}

	/** 获取端口号 */
	@Value("${server.port}")
	String port;

	/**
	 * 定义一个简单接口
	 * @param name
	 * @return
	 */
	@GetMapping("/hi/{name}")
	public String home(@PathVariable String name){
		return "hi " + name + ",I am from port :" + port;
	}
}