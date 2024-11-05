package com.hsj.aft.post.repository;

import com.hsj.aft.domain.entity.post.Post;
import com.hsj.aft.post.dto.PostDto;
import com.hsj.aft.post.dto.QPostDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.hsj.aft.domain.entity.post.QPost.post;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostDto> findPostList(String keyword, String type, Pageable pageable) {
        JPAQuery<PostDto> query = queryFactory
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
                );

        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Post> pathBuilder = new PathBuilder<>(Post.class, "post");
            query.orderBy(
                    new OrderSpecifier<>(
                            order.isAscending() ? Order.ASC : Order.DESC,
                            pathBuilder.getString(order.getProperty())
                    )
            );
        }

        List<PostDto> postList = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        post.deleteYn.eq("N"),
                        searchByType(type, keyword)
                )
                .fetchOne();

        return new PageImpl<>(postList, pageable, total != null ? total : 0L);
    }

    @Override
    public long increaseViewCount(Integer postNo, int viewCount) {
        return queryFactory
                .update(post)
                .set(post.viewCount, post.viewCount.add(viewCount))
                .where(post.postNo.eq(postNo))
                .execute();
    }

    @Override
    public PostDto findPost(Integer postNo) {
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
                        post.postNo.eq(postNo),
                        post.deleteYn.eq("N")
                )
                .fetchOne();
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
