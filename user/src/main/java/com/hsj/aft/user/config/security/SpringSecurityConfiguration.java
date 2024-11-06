package com.hsj.aft.user.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsj.aft.common.dto.CommonResponse;
import com.hsj.aft.user.config.jwt.JwtAuthenticationFilter;
import com.hsj.aft.user.config.jwt.JwtProperties;
import com.hsj.aft.user.config.jwt.JwtTokenProvider;
import com.hsj.aft.user.repository.AuthRepository;
import com.hsj.aft.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Locale;

import static com.hsj.aft.common.constants.Constants.LOGIN_REQUIRED_CODE;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfiguration {

//    /**
//     * 세션을 통한 로그인 인증
//     */
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(AbstractHttpConfigurer::disable)
//            .sessionManagement(session -> session
//                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//            .authorizeHttpRequests(auth -> auth
//                    .requestMatchers("/auth/logout").authenticated()
//                    .requestMatchers("/auth/**").permitAll()
//                    .anyRequest().authenticated()
//            )
//            .exceptionHandling(exception -> exception
//                    .authenticationEntryPoint((request, response, authException) -> {
//                        response.setContentType("application/json;charset=UTF-8");
//                        response.setStatus(HttpServletResponse.SC_OK);
//
//                        CommonResponse errorResponse = CommonResponse.error(LOGIN_REQUIRED_CODE, messageSource.getMessage("message.login.required", null, Locale.KOREA));
//                        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
//
//                        response.getWriter().write(jsonResponse);
//                    })
//            );
//
//        return http.build();
//    }
//

    private final MessageSource messageSource;
    private final JwtProperties jwtProperties;
    private final AuthRepository authRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new AuthService(messageSource, passwordEncoder(), authRepository);
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(jwtProperties, userDetailsService());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/logout").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                            CommonResponse errorResponse = CommonResponse.error(
                                    LOGIN_REQUIRED_CODE,
                                    messageSource.getMessage("message.login.required", null, Locale.KOREA)
                            );
                            String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

                            response.getWriter().write(jsonResponse);
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        return new ProviderManager(
                new DaoAuthenticationProvider() {{
                    setUserDetailsService(userDetailsService);
                    setPasswordEncoder(passwordEncoder);
                }}
        );
    }
}