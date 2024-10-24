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
public class Post extends BaseEntity implements Persistable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableNo;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private int viewCount;

    // 생성자
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.viewCount = 0;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Override
    public String getId() {
        return tableNo.toString();
    }

    @Override
    public boolean isNew() {
        return this.getInsertDate() == null;
    }
}