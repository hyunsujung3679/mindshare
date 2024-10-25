package com.hsj.aft.user.repository;

import com.hsj.aft.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUserId(String userId);

    int countByUserId(String userId);

}
