package com.hsj.aft.user.controller;


import com.hsj.aft.common.dto.CommonResponse;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.UpdateUserReq;
import com.hsj.aft.user.dto.response.UpdateUserRes;
import com.hsj.aft.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{userNo}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable Integer userNo, @RequestBody UpdateUserReq user, Principal principal) {
        UserDto updatedUser = userService.updateUser(userNo, user, principal.getName());
        UpdateUserRes response = new UpdateUserRes();
        response.setUser(updatedUser);
        return new ResponseEntity<>(CommonResponse.success(response), HttpStatus.OK);
    }

}
