package com.hsj.aft.post.dto.response;

import com.hsj.aft.post.dto.PostDto;
import lombok.Data;

import java.util.List;

@Data
public class SelectPostListRes {

    private long total;
    private int totalPages;
    private int currentPage;
    private List<PostDto> postList;
}
