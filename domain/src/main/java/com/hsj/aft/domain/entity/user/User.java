package com.hsj.aft.domain.entity.user;

import com.hsj.aft.domain.entity.base.BaseTimeEntity;
import com.hsj.aft.domain.entity.post.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userNo;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userName;

    @OneToMany(mappedBy = "insertUser")
    private List<Post> insertedPosts = new ArrayList<>();

    @OneToMany(mappedBy = "modifyUser")
    private List<Post> modifiedPosts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insert_user_no")
    private User insertUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_user_no")
    private User modifyUser;

    @OneToMany(mappedBy = "insertUser")
    private List<User> insertedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "modifyUser")
    private List<User> modifiedUsers = new ArrayList<>();

    public User(String userId, String userPassword, String userName, User insertUser, User modifyUser) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.insertUser = insertUser;
        this.modifyUser = modifyUser;
    }

    public void updatePassword(String newPassword) {
        this.userPassword = newPassword;
    }

    public void updateUserName(String newUserName) {
        this.userName = newUserName;
    }

    //==연관관계 메서드==//
    public void updateModifyUser(User user) {
        this.modifyUser = user;
        user.getModifiedUsers().add(this);
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