package com.hsj.aft.user.service;

import com.hsj.aft.domain.entity.User;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.UpdateUserRequest;
import com.hsj.aft.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserDto updateUser(Long userNo, UpdateUserRequest userRequest) {

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userNo));

        if(!userNo.equals(user.getUserNo())) {
            return null;
        }

        if(userRequest.getUserName() != null && !user.getUserName().equals(userRequest.getUserName())) {
            user.updateUserName(userRequest.getUserName());
            user.updateModifyUserNo(user.getUserNo());
        }

        if(userRequest.getUserPassword() != null && !passwordEncoder.matches(userRequest.getUserPassword(), user.getUserPassword())) {
            String encodedPassword = passwordEncoder.encode(userRequest.getUserPassword());
            user.updatePassword(encodedPassword);
            user.updateModifyUserNo(user.getUserNo());
        }

        return UserDto.from(user);
    }
}
