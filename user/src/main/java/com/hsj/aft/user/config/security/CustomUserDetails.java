package com.hsj.aft.user.config.security;

import com.hsj.aft.domain.entity.User;
import lombok.Getter;

import java.util.Collections;

@Getter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private final Integer userNo;

    public CustomUserDetails(User user) {
        super(user.getUserId(), user.getUserPassword(), Collections.emptyList());
        this.userNo = user.getUserNo();
    }

}