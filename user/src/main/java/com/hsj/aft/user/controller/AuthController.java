package com.hsj.aft.user.controller;

import com.hsj.aft.common.dto.CommonResponse;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.LoginReq;
import com.hsj.aft.user.dto.request.SignUpReq;
import com.hsj.aft.user.dto.response.SignUpRes;
import com.hsj.aft.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MessageSource messageSource;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    /**
     * 회원가입
     * @param user
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@Valid @RequestBody SignUpReq user) {
        UserDto signUpUser = authService.signUp(user);

        SignUpRes response = new SignUpRes();
        response.setUser(signUpUser);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    /**
     * 로그인
     *
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(
            @RequestBody LoginReq user,
            HttpServletRequest request) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUserId(),
                            user.getUserPassword()
                    )
            );
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

            return ResponseEntity.ok(CommonResponse.success());
    }

    /**
     * 로그아웃
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(CommonResponse.success());
    }

}
