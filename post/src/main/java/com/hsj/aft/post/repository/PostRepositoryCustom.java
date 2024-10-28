package com.hsj.aft.post.repository;

import com.hsj.aft.post.dto.PostDto;

import java.util.List;

public interface PostRepositoryCustom {

    List<PostDto> findPostList(String keyword, String type);

    long increaseViewCount(Integer postNo);

    PostDto findPost(Integer postNo);
}
