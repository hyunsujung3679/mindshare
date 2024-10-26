package com.hsj.aft.user.service;

import com.hsj.aft.common.exception.DuplicateIdException;
import com.hsj.aft.domain.entity.User;
import com.hsj.aft.user.config.security.*;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.SignUpReq;
import com.hsj.aft.user.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {

    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = authRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(null));

        return new CustomUserDetails(user);
    }

    public UserDto signUp(SignUpReq signUpReq) {
        if(isDuplicateUserId(signUpReq.getUserId())) {
            throw new DuplicateIdException(messageSource.getMessage("message.id.check", null, Locale.KOREA));
        }

        // 회원 가입 시, insertUserNo, modifyUserNo는 admin 계정의 번호로 저장
        User user = authRepository.findById(1)
                .orElseThrow(() -> new UsernameNotFoundException(null));
        String encodedPassword = passwordEncoder.encode(signUpReq.getUserPassword());
        User savedUser = authRepository.save(new User(signUpReq.getUserId(), encodedPassword, signUpReq.getUserName(), user, user));
        return UserDto.from(savedUser);

    }

    private boolean isDuplicateUserId(String userId) {
        return authRepository.countByUserId(userId) > 0;
    }

}
