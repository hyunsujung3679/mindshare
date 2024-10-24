package com.hsj.aft.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

    private String userId;
    private String userPassword;
    private String userName;

}
