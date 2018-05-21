package pers.hanchao.springclouddemo.zipkinpong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableEurekaClient
@RestController
@SpringBootApplication
public class ZipkinPongApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipkinPongApplication.class, args);
	}

	/**
	 *
	 * @return
	 */
	@GetMapping("/pong")
	public String pong(){
		return "pong!";
	}
}
