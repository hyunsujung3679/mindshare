package com.hsj.aft.user.controller;

import com.hsj.aft.common.dto.CommonResponse;
import com.hsj.aft.user.config.jwt.JwtTokenProvider;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.LoginReq;
import com.hsj.aft.user.dto.request.ReissueReq;
import com.hsj.aft.user.dto.request.SignUpReq;
import com.hsj.aft.user.dto.response.LoginRes;
import com.hsj.aft.user.dto.response.ReissueRes;
import com.hsj.aft.user.dto.response.SignUpRes;
import com.hsj.aft.user.service.AuthService;
import com.hsj.aft.user.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final TokenService tokenService;
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
     * @param user
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody LoginReq user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserId(), user.getUserPassword())
        );

        String accessToken = tokenProvider.createAccessToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken();

        tokenService.saveRefreshToken(
                user.getUserId(),
                refreshToken,
                tokenProvider.getRefreshTokenValidityInSeconds() * 1000
        );

        LoginRes tokenRes = new LoginRes(accessToken, refreshToken);
        return ResponseEntity.ok(CommonResponse.success(tokenRes));
    }

    /**
     * 로그아웃
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String accessToken = bearerToken.substring(7);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            tokenService.logout(accessToken, authentication.getName());
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(CommonResponse.success());
    }

    /**
     * Access 토큰 재발행
     * @param request
     * @return
     */
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse> reissue(@RequestBody ReissueReq request) {
        String newAccessToken = tokenService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(CommonResponse.success(new ReissueRes(newAccessToken)));
    }

}
