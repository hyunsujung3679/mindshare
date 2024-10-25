package com.hsj.aft.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InsertPostReq {

    private String title;
    private String content;

}
