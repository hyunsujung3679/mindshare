package com.hsj.aft.user.service;

import com.hsj.aft.common.exception.NoAuthorizationException;
import com.hsj.aft.common.exception.PasswordNotMatchException;
import com.hsj.aft.domain.entity.user.User;
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

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public UserDto updateUser(Integer userNo, UpdateUserReq userReq, Integer sessionUserNo) {

        User user = userRepository.findById(userNo)
                .orElseThrow(() -> new EntityNotFoundException(messageSource.getMessage("message.user.not.found", null, Locale.KOREA)));

        if(!sessionUserNo.equals(user.getUserNo())) {
            throw new NoAuthorizationException(messageSource.getMessage("message.no.authorization", null, Locale.KOREA));
        }

        if(userReq.getUserName() != null && !user.getUserName().equals(userReq.getUserName())) {
            user.updateUserName(userReq.getUserName());
            user.updateModifyUser(user);
        }

        if(userReq.getCurrentPassword() != null && userReq.getNewPassword() != null) {
            if(!passwordEncoder.matches(userReq.getCurrentPassword(), user.getUserPassword())) {
                throw new PasswordNotMatchException(messageSource.getMessage("message.current.password.not.match", null, Locale.KOREA));
            }
            if(userReq.getCurrentPassword().equals(userReq.getNewPassword())) {
                throw new PasswordNotMatchException(messageSource.getMessage("message.current.password.not.match", null, Locale.KOREA));
            }

            String encodedNewPassword = passwordEncoder.encode(userReq.getNewPassword());
            user.updatePassword(encodedNewPassword);
            user.updateModifyUser(user);
        }

        return UserDto.from(user);
    }

}
