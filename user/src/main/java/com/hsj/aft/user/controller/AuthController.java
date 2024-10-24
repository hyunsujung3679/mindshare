package com.hsj.aft.user.controller;

import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.LoginRequest;
import com.hsj.aft.user.dto.request.SignUpRequest;
import com.hsj.aft.user.dto.response.CommonResponse;
import com.hsj.aft.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.hsj.aft.user.common.Constants.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignUpRequest user) {
        UserDto signUpUser = authService.signUp(user);
        if(signUpUser == null) {
            return new ResponseEntity<>(CommonResponse.error(ID_CHECK_CODE, ID_CHECK_MESSAGE), HttpStatus.OK);
        }
        return new ResponseEntity<>(CommonResponse.success(signUpUser), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody LoginRequest user, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserId(),
                            user.getUserPassword()
                    )
            );
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);  // 이 키를 사용해야 Spring Security가 인식

            return new ResponseEntity<>(CommonResponse.success(), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(CommonResponse.error(LOGIN_FAIL_CODE, LOGIN_FAIL_MESSAGE), HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.OK);
    }

}
