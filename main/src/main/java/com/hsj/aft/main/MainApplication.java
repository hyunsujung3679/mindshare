package com.hsj.aft.main;

import com.hsj.aft.user.config.security.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

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
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	public AuditorAware<Integer> auditorProvider() {
		return () -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication == null || !authentication.isAuthenticated()) {
				return Optional.empty();
			}

			if (authentication.getPrincipal() instanceof CustomUserDetails) {
				CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
				return Optional.of(userDetails.getUserNo());
			}

			return Optional.empty();
		};
	}
}