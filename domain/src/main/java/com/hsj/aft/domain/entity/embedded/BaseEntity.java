package com.hsj.aft.domain.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@Setter
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(name = "insert_user_no", updatable = false)
    private Integer insertUserNo;

    @LastModifiedBy
    @Column(name = "modify_user_no")
    private Integer modifyUserNo;

}

