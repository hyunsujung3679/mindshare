package com.hsj.aft.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserReq {

    private String currentPassword;
    private String newPassword;
    private String userName;

}
