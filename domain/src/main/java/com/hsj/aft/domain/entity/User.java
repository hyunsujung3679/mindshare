package com.hsj.aft.domain.entity;

import com.hsj.aft.domain.entity.embedded.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNo;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userName;

    public User(String userId, String userPassword, String userName) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public void updatePassword(String newPassword) {
        this.userPassword = newPassword;
    }

    public void updateUserName(String newUserName) {
        this.userName = newUserName;
    }

    @Override
    public Integer getId() {
        return userNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}