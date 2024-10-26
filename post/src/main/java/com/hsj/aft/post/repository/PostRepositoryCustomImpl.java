package com.hsj.aft.post.repository;

import com.hsj.aft.domain.entity.Post;
import com.hsj.aft.post.dto.QPostDto;
import com.hsj.aft.post.dto.PostDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.hsj.aft.domain.entity.QPost.post;
import static com.hsj.aft.domain.entity.QUser.user;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PostDto> findPostList(String keyword, String type) {
        return queryFactory
                .select(new QPostDto(
                        post.postNo,
                        post.title,
                        post.content,
                        post.viewCount,
                        post.deleteYn,
                        post.insertUser.userId,
                        post.insertDate,
                        post.modifyUser.userId,
                        post.modifyDate
                ))
                .from(post)
                .join(post.insertUser)
                .join(post.modifyUser)
                .where(
                        post.deleteYn.eq("N"),
                        searchByType(type, keyword)
                )
                .fetch();
    }

    private BooleanExpression searchByType(String type, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return switch (type) {
            case "title" -> post.title.contains(keyword);
            case "content" -> post.content.contains(keyword);
            case "insertId" -> post.insertUser.userId.contains(keyword);
            case "modifyId" -> post.modifyUser.userId.contains(keyword);
            default -> null;
        };
    }
}