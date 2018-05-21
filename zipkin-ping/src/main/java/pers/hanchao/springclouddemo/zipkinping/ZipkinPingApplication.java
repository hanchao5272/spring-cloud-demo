package pers.hanchao.springclouddemo.zipkinping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableEurekaClient
@SpringBootApplication
public class ZipkinPingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinPingApplication.class, args);
	}

	@GetMapping("/ping")
	public String ping(){
		return "ping!";
	}
}
