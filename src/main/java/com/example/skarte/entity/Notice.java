package com.example.skarte.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
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
//public class Notice extends AbstractEntity implements Serializable {
//    private static final long serialVersionUID = 1L;

    /** ID */
//    @Id
//    @SequenceGenerator(name = "notice_id_seq")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @Id
    @Column
    @SequenceGenerator(name = "notices_noticeId_seq", sequenceName = "notices_noticeId_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notices_noticeId_seq")
    private Long noticeId = null;

    public void setId(Long noticeId) {
        this.noticeId = null;
    }

    /** タイトル */
    @Column(length = 20, nullable = false)
    @NotEmpty
    private String title = null;

    /** 内容 */
    @Column(length = 1000, nullable = false)
    private String contents = null;

}