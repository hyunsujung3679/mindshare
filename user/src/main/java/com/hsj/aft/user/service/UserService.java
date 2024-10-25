package com.hsj.aft.user.service;

import com.hsj.aft.common.exception.DuplicateIdException;
import com.hsj.aft.common.exception.NoAuthorizationException;
import com.hsj.aft.domain.entity.User;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.UpdateUserReq;
import com.hsj.aft.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserDto updateUser(Integer userNo, UpdateUserReq userReq, String userId) {

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", new Object[]{userNo}, Locale.KOREA)));

        if(!userId.equals(user.getUserId())) {
            throw new NoAuthorizationException(messageSource.getMessage("message.no.authorization", null, Locale.KOREA));
        }

        if(userReq.getUserName() != null && !user.getUserName().equals(userReq.getUserName())) {
            user.updateUserName(userReq.getUserName());
        }

        if(userReq.getUserPassword() != null && !passwordEncoder.matches(userReq.getUserPassword(), user.getUserPassword())) {
            String encodedPassword = passwordEncoder.encode(userReq.getUserPassword());
            user.updatePassword(encodedPassword);
        }

        return UserDto.from(user);
    }
}
