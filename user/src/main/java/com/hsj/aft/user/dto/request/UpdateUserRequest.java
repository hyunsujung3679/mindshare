package com.hsj.aft.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private String userPassword;
    private String userName;

}
