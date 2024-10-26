package com.hsj.aft.post.dto;

import com.hsj.aft.domain.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDto {

    private Integer postNo;
    private String title;
    private String content;
    private Integer viewCount;
    private String deleteYn;
    private String insertId;
    private LocalDateTime insertDate;
    private String modifyId;
    private LocalDateTime modifyDate;

    public PostDto() {}

    @QueryProjection
    public PostDto(Integer postNo, String title, String content, Integer viewCount, String deleteYn, String insertId, LocalDateTime insertDate, String modifyId, LocalDateTime modifyDate) {
        this.postNo = postNo;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.deleteYn = deleteYn;
        this.insertId = insertId;
        this.insertDate = insertDate;
        this.modifyId = modifyId;
        this.modifyDate = modifyDate;
    }

    public static PostDto from(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostNo(post.getPostNo());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setViewCount(post.getViewCount());
        postDto.setDeleteYn(post.getDeleteYn());
        postDto.setInsertDate(post.getInsertDate());
        postDto.setModifyDate(post.getModifyDate());
        return postDto;
    }
}
