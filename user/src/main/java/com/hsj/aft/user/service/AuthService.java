package com.hsj.aft.user.service;

import com.hsj.aft.domain.entity.User;
import com.hsj.aft.user.dto.UserDto;
import com.hsj.aft.user.dto.request.SignUpRequest;
import com.hsj.aft.user.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final AuthRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUserId(),
                user.getUserPassword(),
                Collections.emptyList()
        );
    }

    public UserDto signUp(SignUpRequest user) {
        if(isDuplicateUserId(user.getUserId())) return null;
        String encodedPassword = passwordEncoder.encode(user.getUserPassword());
        User savedUser = userRepository.save(new User(user.getUserId(), encodedPassword, user.getUserName(), 1L, 1L));
        return UserDto.from(savedUser);
    }

    private boolean isDuplicateUserId(String userId) {
        return userRepository.countByUserId(userId) > 0;
    }

}
