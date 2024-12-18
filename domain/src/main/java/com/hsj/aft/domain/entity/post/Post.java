package com.hsj.aft.domain.entity.post;

import com.hsj.aft.domain.entity.user.User;
import com.hsj.aft.domain.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity implements Persistable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postNo;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private String deleteYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insert_user_no", updatable = false)
    private User insertUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modify_user_no")
    private User modifyUser;

    public Post(String title, String content, User insertUser, User modifyUser) {
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.deleteYn = "N";
        this.insertUser = insertUser;
        this.modifyUser = modifyUser;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDeleteYn(String deleteYn) {
        this.deleteYn = deleteYn;
    }

    //==연관관계 메서드==//
    public void updateModifyUser(User user) {
        this.modifyUser = user;
        user.getModifiedPosts().add(this);
    }

    @Override
    public Integer getId() {
        return postNo;
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}