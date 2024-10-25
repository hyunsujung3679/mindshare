package com.hsj.aft.post.service;

import com.hsj.aft.common.exception.NoAuthorizationException;
import com.hsj.aft.domain.entity.Post;
import com.hsj.aft.post.dto.PostDto;
import com.hsj.aft.post.dto.request.InsertPostReq;
import com.hsj.aft.post.dto.request.UpdatePostReq;
import com.hsj.aft.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final MessageSource messageSource;
    private final PostRepository postRepository;

    public PostDto insertPost(InsertPostReq post) {
        Post savedPost = postRepository.save(new Post(post.getTitle(), post.getContent()));
        return PostDto.from(savedPost);
    }

    public List<PostDto> selectPostList() {
        List<Post> postList = postRepository.findAllByDeleteYnOrderByPostNoDesc("N");
        return postList.stream()
                .map(PostDto::from)
                .collect(Collectors.toList());
    }

    public List<PostDto> searchPostList(String keyword, String type) {
        List<Post> postList = postRepository.findListBySearch(keyword, type);
        return postList.stream()
                .map(PostDto::from)
                .collect(Collectors.toList());
    }

    public Post selectPost(Integer postNo) {
        Post post = postRepository.findOneByIdAndDeleteYn(postNo, "N").
                orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", new Object[]{postNo}, Locale.KOREA)));
        post.increaseViewCount();
        return post;
    }

    public PostDto updatePost(Integer postNo, UpdatePostReq postReq, Integer userNo) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", new Object[]{postNo}, Locale.KOREA)));

        if(userNo != post.getInsertUserNo()) {
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

        return PostDto.from(post);
    }

    public PostDto deletePost(Integer postNo, Integer userNo) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.post.not.found", new Object[]{postNo}, Locale.KOREA)));

        if(userNo != post.getInsertUserNo()) {
            throw new NoAuthorizationException(messageSource.getMessage("message.no.authorization", null, Locale.KOREA));
        }

        post.updateDeleteYn("Y");

        return PostDto.from(post);
    }
}
