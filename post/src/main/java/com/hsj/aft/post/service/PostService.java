package com.hsj.aft.post.service;

import com.hsj.aft.common.exception.NoAuthorizationException;
import com.hsj.aft.domain.entity.Post;
import com.hsj.aft.domain.entity.User;
import com.hsj.aft.post.dto.PostDto;
import com.hsj.aft.post.dto.request.InsertPostReq;
import com.hsj.aft.post.dto.request.UpdatePostReq;
import com.hsj.aft.post.repository.PostRepository;
import com.hsj.aft.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final MessageSource messageSource;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostDto insertPost(InsertPostReq post, Integer userNo) {
        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{userNo}, Locale.KOREA)));

        Post savedPost = postRepository.save(new Post(post.getTitle(), post.getContent(), user, user));

        return PostDto.from(savedPost);
    }

    public List<PostDto> selectPostList() {
        return postRepository.findPostList(null, null);
    }

    public List<PostDto> selectPostList(String keyword, String type) {
        return postRepository.findPostList(keyword, type);
    }

    public PostDto selectPost(Integer postNo) {
        Post post = postRepository.findOneByIdAndDeleteYn(postNo, "N")
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", new Object[]{postNo}, Locale.KOREA)));

        post.increaseViewCount();

        User insertUser = userRepository.findById(post.getInsertUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{post.getInsertUser().getUserNo()}, Locale.KOREA)));

        User modifyUser = userRepository.findById(post.getModifyUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{post.getModifyUser().getUserNo()}, Locale.KOREA)));

        PostDto postDto = PostDto.from(post);
        postDto.setInsertId(insertUser.getUserId());
        postDto.setModifyId(modifyUser.getUserId());

        return postDto;

    }

    public PostDto updatePost(Integer postNo, UpdatePostReq postReq, Integer userNo) {
        Post post = postRepository.findOneByIdAndDeleteYn(postNo, "N")
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", new Object[]{postNo}, Locale.KOREA)));

        if(userNo != post.getInsertUser().getUserNo()) {
            throw new NoAuthorizationException(messageSource.getMessage("message.no.authorization", null, Locale.KOREA));
        }

        if(postReq.getTitle() != null && !postReq.getTitle().equals(post.getTitle())) {
            post.updateTitle(postReq.getTitle());
        }

        if(postReq.getContent() != null && !postReq.getContent().equals(post.getContent())) {
            post.updateContent(postReq.getContent());
        }

        if(postReq.getDeleteYn() != null && !postReq.getDeleteYn().equals(post.getDeleteYn())) {
            post.updateDeleteYn(postReq.getDeleteYn());
        }

        User insertUser = userRepository.findById(post.getInsertUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{post.getInsertUser().getUserNo()}, Locale.KOREA)));

        User modifyUser = userRepository.findById(post.getModifyUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{post.getModifyUser().getUserNo()}, Locale.KOREA)));

        PostDto postDto = PostDto.from(post);
        postDto.setInsertId(insertUser.getUserId());
        postDto.setModifyId(modifyUser.getUserId());

        return postDto;
    }

    public PostDto deletePost(Integer postNo, Integer userNo) {
        Post post = postRepository.findOneByIdAndDeleteYn(postNo, "N")
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", new Object[]{postNo}, Locale.KOREA)));

        if(userNo != post.getInsertUser().getUserNo()) {
            throw new NoAuthorizationException(messageSource.getMessage("message.no.authorization", null, Locale.KOREA));
        }

        post.updateDeleteYn("Y");

        User insertUser = userRepository.findById(post.getInsertUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{post.getInsertUser().getUserNo()}, Locale.KOREA)));

        User modifyUser = userRepository.findById(post.getModifyUser().getUserNo())
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{post.getModifyUser().getUserNo()}, Locale.KOREA)));

        PostDto postDto = PostDto.from(post);
        postDto.setInsertId(insertUser.getUserId());
        postDto.setModifyId(modifyUser.getUserId());

        return postDto;
    }
}