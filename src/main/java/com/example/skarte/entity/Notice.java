package com.example.skarte.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * お知らせ
 */
@Entity
@Table(name = "notices")
@Data
@EqualsAndHashCode(callSuper = false) //AbstractEntityから
public class Notice extends AbstractEntity { //AbstractEntityから

    /** ID */
    @Id
    @Column
    @SequenceGenerator(name = "notices_notice_id_seq", sequenceName = "notices_notice_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notices_notice_id_seq")
    private Long noticeId = null;

    public void setId(Long noticeId) {
        this.noticeId = null;
    }
    
    /** タイトル */
    @Column(length = 20, nullable = false)
    private String title = null;

    /** 内容 */
    @Column(length = 1000, nullable = false)
    private String contents = null;

    /** 作成者 */
    @Transient
    private String createdBy = null;

    /** 更新者 */
    @Transient
    private String updatedBy = null;


    /** 削除済 */
    private boolean deleted = false;

}