package com.hsj.aft.post.repository;

import com.hsj.aft.domain.entity.Post;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.hsj.aft.domain.entity.QPost.post;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Post> findListBySearch(String keyword, String type) {
        return queryFactory
                .selectFrom(post)
                .where(
                        post.deleteYn.eq("N"),
                        searchByType(type, keyword)
                )
                .orderBy(post.postNo.desc())
                .fetch();
    }

    private BooleanExpression searchByType(String type, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        switch (type) {
            case "title":
                return post.title.contains(keyword);
            case "content":
                return post.content.contains(keyword);
            case "insertUserNo":
                try {
                    int userNo = Integer.parseInt(keyword);
                    return post.insertUserNo.eq(userNo);
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }
}
