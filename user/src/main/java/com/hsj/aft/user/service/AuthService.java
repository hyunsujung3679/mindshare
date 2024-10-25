package com.hsj.aft.user.service;

import com.hsj.aft.domain.entity.User;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.SignUpReq;
import com.hsj.aft.user.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hsj.aft.user.config.security.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = authRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }

    public UserDto signUp(SignUpReq user) {
        if(isDuplicateUserId(user.getUserId())) return null;
        String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        User savedUser = authRepository.save(new User(user.getUserId(), encodedPassword, user.getUserName()));
        return UserDto.from(savedUser);
    }

    private boolean isDuplicateUserId(String userId) {
        return authRepository.countByUserId(userId) > 0;
    }

}
