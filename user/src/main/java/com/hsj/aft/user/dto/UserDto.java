package com.hsj.aft.user.dto;

import com.hsj.aft.domain.entity.User;
import lombok.Data;

@Data
public class UserDto {

    private Long userNo;
    private String userId;
    private String userName;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserNo(user.getUserNo());
        userDto.setUserId(user.getUserId());
        userDto.setUserName(user.getUserName());
        return userDto;
    }
}
