package com.hsj.aft.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(
		scanBasePackages = {
				"com.hsj.aft.domain",  // domain 모듈 스캔
				"com.hsj.aft.user"     // user 모듈 스캔
		}
)
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
