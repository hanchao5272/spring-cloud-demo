package pers.hanchao.springclouddemo.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableEurekaClient
@RestController
@SpringBootApplication
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}

	/** 获取配置中心的配置 */
	@Value("${message}")
	private String message;

	/**
	 * 返回配置
	 * @return
	 */
	@GetMapping("/hi")
	public String hi(){
		return message;
	}
}
