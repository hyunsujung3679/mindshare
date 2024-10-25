package com.hsj.aft.post.dto;

import com.hsj.aft.domain.entity.Post;
import lombok.Data;

@Data
public class PostDto {

    private int PostNo;
    private String title;
    private String content;
    private int viewCount;
    private String deleteYn;

    public static PostDto from(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostNo(post.getPostNo());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setViewCount(post.getViewCount());
        postDto.setDeleteYn(post.getDeleteYn());
        return postDto;
    }
}
