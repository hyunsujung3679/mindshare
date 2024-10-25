package com.hsj.aft.post.repository;

import com.hsj.aft.domain.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findListBySearch(String keyword, String type);

}
