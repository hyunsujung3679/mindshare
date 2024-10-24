package com.hsj.aft.user.repository;

import com.hsj.aft.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {



}
