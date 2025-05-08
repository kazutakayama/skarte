package com.example.skarte.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * お知らせ
 */
@Entity
@Table(name = "notices")
@Data
@EqualsAndHashCode(callSuper = false) // EntityBase
public class Notice extends EntityBase { // EntityBase

    /** ID */
    @Id
    @Column
    @SequenceGenerator(name = "notices_noticeId_seq", sequenceName = "notices_noticeId_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notices_noticeId_seq")
    private Long noticeId = null;

    public void setId(Long noticeId) {
        this.noticeId = null;
    }

    /** 内容 */
    @Column
    private String contents = null;

}