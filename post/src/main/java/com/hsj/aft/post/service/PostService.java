package com.hsj.aft.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hsj.aft.common.exception.NoAuthorizationException;
import com.hsj.aft.domain.entity.post.Post;
import com.hsj.aft.domain.entity.user.User;
import com.hsj.aft.post.dto.PostDto;
import com.hsj.aft.post.dto.page.CustomPage;
import com.hsj.aft.post.dto.request.InsertPostReq;
import com.hsj.aft.post.dto.request.UpdatePostReq;
import com.hsj.aft.post.repository.PostRepository;
import com.hsj.aft.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

import static com.hsj.aft.common.constants.Constants.LIST_CACHE_TTL;
import static com.hsj.aft.common.constants.Constants.POST_CACHE_TTL;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RedisCacheService cacheService;
    private final MessageSource messageSource;

    public PostDto insertPost(InsertPostReq post, Integer userNo) {
        User user = userRepository.findById(userNo).orElseThrow(() ->
                new EntityNotFoundException(messageSource.getMessage("message.user.not.found", null, Locale.KOREA)));

        Post savedPost = postRepository.save(new Post(post.getTitle(), post.getContent(), user, user));
        PostDto postDto = getPostDto(savedPost);

        cacheService.deleteAllPostListCaches();

        return postDto;
    }

    public Page<PostDto> selectPostList(String keyword, String type, Pageable pageable) {
        String cacheKey = cacheService.getListCacheKey(keyword, type, pageable);

        CustomPage<PostDto> cachedPage = cacheService.getCache(cacheKey, new TypeReference<CustomPage<PostDto>>() {});

        if (cachedPage != null) {
            return cachedPage.toPage();
        }

        Page<PostDto> postList = postRepository.findPostList(keyword, type, pageable);

        cacheService.setCache(cacheKey, CustomPage.from(postList), LIST_CACHE_TTL);

        return postList;
    }

    public PostDto selectPost(Integer postNo) {
        cacheService.incrementViewCount(postNo);

        String cacheKey = cacheService.getPostCacheKey(postNo);
        PostDto post = cacheService.getCache(cacheKey, new TypeReference<PostDto>() {});

        if (post == null) {
            post = postRepository.findPost(postNo);
            if (post == null) {
                throw new EntityNotFoundException(messageSource.getMessage("message.post.not.found", null, Locale.KOREA));
            }
            cacheService.setCache(cacheKey, post, POST_CACHE_TTL);
        }

        Integer viewCount = cacheService.getViewCount(postNo);
        if (viewCount > 0) {
            post.setViewCount(post.getViewCount() + viewCount);
        }

        return post;
    }

    public PostDto updatePost(Integer postNo, UpdatePostReq postReq, Integer userNo) {
        Post post = postRepository.findOneByIdAndDeleteYn(postNo, "N")
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", null, Locale.KOREA)));

        if(userNo != post.getInsertUser().getUserNo()) {
            throw new NoAuthorizationException(messageSource.getMessage("message.no.authorization", null, Locale.KOREA));
        }

        updatePostFields(post, postReq);

        cacheService.deleteAllPostListCaches();
        cacheService.deletePostCaches(postNo);

        return getPostDto(post);
    }

    public PostDto deletePost(Integer postNo, Integer userNo) {
        Post post = postRepository.findOneByIdAndDeleteYn(postNo, "N")
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", null, Locale.KOREA)));

        if(userNo != post.getInsertUser().getUserNo()) {
            throw new NoAuthorizationException(messageSource.getMessage("message.no.authorization", null, Locale.KOREA));
        }

        post.updateDeleteYn("Y");

        cacheService.deleteAllPostListCaches();
        cacheService.deletePostCaches(postNo);

        return getPostDto(post);
    }

    private void updatePostFields(Post post, UpdatePostReq postReq) {
        if(postReq.getTitle() != null && !postReq.getTitle().equals(post.getTitle())) {
            post.updateTitle(postReq.getTitle());
        }

        if(postReq.getContent() != null && !postReq.getContent().equals(post.getContent())) {
            post.updateContent(postReq.getContent());
        }

        if(postReq.getDeleteYn() != null && !postReq.getDeleteYn().equals(post.getDeleteYn())) {
            post.updateDeleteYn(postReq.getDeleteYn());
        }
    }

    private PostDto getPostDto(Post post) {
        User insertUser = userRepository.findById(post.getInsertUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", null, Locale.KOREA)));

        User modifyUser = userRepository.findById(post.getModifyUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", null, Locale.KOREA)));

        PostDto postDto = PostDto.from(post);
        postDto.setInsertId(insertUser.getUserId());
        postDto.setModifyId(modifyUser.getUserId());
        return postDto;
    }
}