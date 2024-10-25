package com.hsj.aft.post.dto.response;

import com.hsj.aft.post.dto.PostDto;
import lombok.Data;

import java.util.List;

@Data
public class SelectPostListRes {

    private int total;
    private List<PostDto> postList;
}
