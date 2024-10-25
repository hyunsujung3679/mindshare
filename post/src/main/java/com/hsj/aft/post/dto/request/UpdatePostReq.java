package com.hsj.aft.post.dto.request;

import lombok.Data;

@Data
public class UpdatePostReq {

    private String title;
    private String content;
    private String deleteYn;

}
