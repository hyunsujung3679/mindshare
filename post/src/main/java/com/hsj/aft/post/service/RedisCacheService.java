package com.hsj.aft.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsj.aft.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hsj.aft.common.constants.Constants.*;

import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PostRepository postRepository;

    public <T> void setCache(String key, T data, Duration ttl) {
        try {
            String serializedData = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, serializedData, ttl);
        } catch (Exception e) {
            log.error("Error caching data: {}", key, e);
        }
    }

    public <T> T getCache(String key, TypeReference<T> typeReference) {
        try {
            String cachedData = redisTemplate.opsForValue().get(key);
            if (cachedData != null) {
                return objectMapper.readValue(cachedData, typeReference);
            }
        } catch (Exception e) {
            log.error("Error reading from cache: {}", key, e);
            redisTemplate.delete(key);
        }
        return null;
    }

    public void incrementViewCount(Integer postNo) {
        String key = String.format(VIEW_COUNT_KEY, postNo);
        redisTemplate.opsForValue().increment(key);
    }

    public Integer getViewCount(Integer postNo) {
        try {
            String key = String.format(VIEW_COUNT_KEY, postNo);
            String value = redisTemplate.opsForValue().get(key);
            return value != null ? Integer.parseInt(value) : 0;
        } catch (Exception e) {
            log.error("Error getting view count for post: {}", postNo, e);
            return 0;
        }
    }

    public void deleteCache(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Error deleting cache with key: {}", key, e);
        }
    }

    public void deleteAllPostListCaches() {
        try {
            Set<String> keys = redisTemplate.keys("post:list:*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            log.error("Error clearing list caches", e);
        }
    }

    public void deletePostCaches(Integer postNo) {
        try {
            deleteCache(getPostCacheKey(postNo));
            deleteCache(getViewCountKey(postNo));
            deleteAllPostListCaches();
        } catch (Exception e) {
            log.error("Error deleting caches for post: {}", postNo, e);
        }
    }

    public String getPostCacheKey(Integer postNo) {
        return String.format(POST_CACHE_KEY, postNo);
    }

    public String getViewCountKey(Integer postNo) {
        return String.format(VIEW_COUNT_KEY, postNo);
    }

    public String getListCacheKey(String type, String keyword, Pageable pageable) {
        return String.format(LIST_CACHE_KEY,
                type != null ? type : "none",
                keyword != null ? keyword : "none",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort().toString().replace(" ", "_"),
                pageable.getSort().getOrderFor("postNo") != null ?
                        pageable.getSort().getOrderFor("postNo").getDirection() : "none"
        );
    }

    @Transactional
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void synchronizeViewCounts() {
        try {
            Set<String> keys = redisTemplate.keys("post:view:*");
            if (keys == null || keys.isEmpty()) {
                return;
            }

            for (String key : keys) {
                try {
                    Integer postNo = Integer.valueOf(key.split(":")[2]);
                    String viewCount = redisTemplate.opsForValue().get(key);

                    if (viewCount != null && !viewCount.isEmpty()) {
                        long success = postRepository.increaseViewCount(postNo, Integer.parseInt(viewCount));
                        if (success > 0) {
                            redisTemplate.delete(key);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error synchronizing view count for key: {}", key, e);
                }
            }
        } catch (Exception e) {
            log.error("Error during view count synchronization", e);
        }
    }
}