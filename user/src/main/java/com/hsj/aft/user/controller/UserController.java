package com.hsj.aft.user.controller;


import com.hsj.aft.common.dto.CommonResponse;
import com.hsj.aft.user.config.security.*;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.UpdateUserReq;
import com.hsj.aft.user.dto.response.UpdateUserRes;
import com.hsj.aft.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 개인정보수정
     * @param userNo
     * @param user
     * @param userDetails
     * @return
     */
    @PatchMapping("/{userNo}")
    public ResponseEntity<CommonResponse> updateUser(
            @PathVariable Integer userNo,
            @Valid @RequestBody UpdateUserReq user,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserDto updatedUser = userService.updateUser(userNo, user, userDetails.getUserNo());

        UpdateUserRes response = new UpdateUserRes();
        response.setUser(updatedUser);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
