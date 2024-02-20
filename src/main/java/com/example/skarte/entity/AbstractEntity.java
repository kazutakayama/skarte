package com.example.skarte.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

/**
 * データベースの作成日時・更新日時共通設定
 */

@MappedSuperclass
@Data
public class AbstractEntity {
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    public void onPrePersist() {
        Date date = new Date();
        setCreatedAt(date);
        setUpdatedAt(date);
    }

    @PreUpdate
    public void onPreUpdate() {
        setUpdatedAt(new Date());
    }
}