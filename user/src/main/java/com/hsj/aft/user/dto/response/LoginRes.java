package com.hsj.aft.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRes {

    private String accessToken;
    private String refreshToken;

}
