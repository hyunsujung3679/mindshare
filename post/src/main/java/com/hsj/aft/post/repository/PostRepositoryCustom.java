package com.hsj.aft.post.repository;

import com.hsj.aft.post.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostDto> findPostList(String keyword, String type, Pageable pageable);

    long increaseViewCount(Integer postNo, int viewCount);

    PostDto findPost(Integer postNo);
}
