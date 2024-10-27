package com.hsj.aft.user.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserReq {

    private String currentPassword;
    @Size(min = 8, max = 16, message = "{message.new.user.password.size}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]*$",
            message = "{message.new.user.password.pattern}")
    private String newPassword;
    @Size(min = 2, max = 10, message = "{message.user.name.size}")
    private String userName;

}
