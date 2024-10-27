package com.hsj.aft.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpReq {

    @NotBlank(message = "{message.user.id.not blank}")
    @Size(min = 4, max = 12, message = "{message.user.id.size}")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "{message.user.id.pattern}")
    private String userId;

    @NotBlank(message = "{message.user.password.not.blank}")
    @Size(min = 8, max = 16, message = "{message.user.password.size}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]*$",
            message = "{message.user.password.pattern}")
    private String userPassword;

    @NotBlank(message = "{message.user.name.not.blank}")
    @Size(min = 2, max = 10, message = "{message.user.name.size}")
    private String userName;

}
