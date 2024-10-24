package com.hsj.aft.domain.entity;

import com.hsj.aft.domain.entity.embedded.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements Persistable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private Long insertUserNo;

    @Column(nullable = false)
    private Long modifyUserNo;

    public User(String userId, String userPassword, String userName, Long insertUserNo, Long modifyUserNo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.insertUserNo = insertUserNo;
        this.modifyUserNo = modifyUserNo;
    }

    public void updatePassword(String newPassword) {
        this.userPassword = newPassword;
    }

    public void updateUserName(String newUserName) {
        this.userName = newUserName;
    }

    public void updateModifyUserNo(Long modifyUserNo) {
        this.modifyUserNo = modifyUserNo;
    }

    @Override
    public String getId() {
        return userNo.toString();
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}