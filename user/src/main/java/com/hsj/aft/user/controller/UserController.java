package com.hsj.aft.user.controller;

import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.UpdateUserRequest;
import com.hsj.aft.user.dto.response.CommonResponse;
import com.hsj.aft.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{userNo}")
    public ResponseEntity<CommonResponse> updateUser(@PathVariable Long userNo, @RequestBody UpdateUserRequest user, HttpSession session) {
        UserDto updateUser = userService.updateUser(userNo, user);
        if(updateUser == null) {
            return new ResponseEntity<>(CommonResponse.error(updateUser), HttpStatus.OK);
        }
        return new ResponseEntity<>(CommonResponse.success(updateUser), HttpStatus.OK);
    }

}
