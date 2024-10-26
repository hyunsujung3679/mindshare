package com.hsj.aft.post.repository;

import com.hsj.aft.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer>, PostRepositoryCustom {

    Optional<Post> findOneByIdAndDeleteYn(Integer postNo, String deleteYn);

}
