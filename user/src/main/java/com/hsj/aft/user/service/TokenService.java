package com.hsj.aft.user.service;

import com.hsj.aft.common.exception.InvalidRefreshTokenException;
import com.hsj.aft.user.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.hsj.aft.common.constants.Constants.BLACKLIST_PREFIX;
import static com.hsj.aft.common.constants.Constants.REFRESH_TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    public void saveRefreshToken(String userId, String refreshToken, Long expiration) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                expiration,
                TimeUnit.MILLISECONDS
        );
    }

    public String getRefreshToken(String userId) {
        String key = REFRESH_TOKEN_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    private void addToBlacklist(String accessToken, Long remainingTime) {
        String key = BLACKLIST_PREFIX + accessToken;
        redisTemplate.opsForValue().set(
                key,
                "logout",
                remainingTime,
                TimeUnit.MILLISECONDS
        );
    }

    public void logout(String accessToken, String userId) {
        redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);

        Long remainingTime = jwtTokenProvider.getRemainingTime(accessToken);
        if (remainingTime > 0) {
            addToBlacklist(accessToken, remainingTime);
        }
    }

    public boolean isTokenBlacklisted(String accessToken) {
        String key = BLACKLIST_PREFIX + accessToken;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public String reissueAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidRefreshTokenException(messageSource.getMessage("message.token.invalid", null, Locale.KOREA));
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String userId = authentication.getName();

        String savedToken = getRefreshToken(userId);
        if (savedToken == null || !savedToken.equals(refreshToken)) {
            throw new InvalidRefreshTokenException(messageSource.getMessage("message.token.invalid", null, Locale.KOREA));
        }

        return jwtTokenProvider.createAccessToken(authentication);
    }
}
