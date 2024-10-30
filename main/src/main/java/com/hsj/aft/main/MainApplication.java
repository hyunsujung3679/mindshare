package com.hsj.aft.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication(
		scanBasePackages = {
				"com.hsj.aft.domain",
				"com.hsj.aft.user",
				"com.hsj.aft.post",
				"com.hsj.aft.common"
		}
)
@EnableJpaRepositories(basePackages = {
		"com.hsj.aft.user.repository",
		"com.hsj.aft.post.repository"
})
@EntityScan(basePackages = "com.hsj.aft.domain.entity")
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}