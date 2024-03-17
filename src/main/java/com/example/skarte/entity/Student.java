package com.example.skarte.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = false) // AbstractEntity
@Builder // CSV
@NoArgsConstructor // CSV
@AllArgsConstructor // CSV

public class Student extends AbstractEntity { // AbstractEntity

    /** ID */
    @Id
    @Column
    private Long studentId = null;

    /** 姓 */
    @Column(length = 20, nullable = false)
    private String lastName = null;

    /** 名 */
    @Column(length = 20, nullable = false)
    private String firstName = null;

    /** せい */
    @Column(length = 20, nullable = false)
    private String lastNameKana = null;

    /** めい */
    @Column(length = 20, nullable = false)
    private String firstNameKana = null;

    /** 生年月日 */
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth = null;

    /** 性別 */
    @Column
    private Integer gender = null;

    /** 家族1 */
    @Column
    private String family1 = null;

    /** 家族2 */
    @Column
    private String family2 = null;

    /** 家族3 */
    @Column
    private String family3 = null;

    /** 家族4 */
    @Column
    private String family4 = null;

    /** 電話1 */
    @Column
    private Long tel1 = null;

    /** 電話2 */
    @Column
    private Long tel2 = null;

    /** 電話3 */
    @Column
    private Long tel3 = null;

    /** 電話4 */
    @Column
    private Long tel4 = null;

    /** 郵便番号 */
    @Column
    private Long postalCode = null;

    /** 住所 */
    @Column
    private String adress = null;

    /** メモ */
    @Column(length = 1000, nullable = false)
    private String memo = null;

    /** 作成者 */
    @Transient
    private String createdBy = null;

    /** 更新者 */
    @Transient
    private String updatedBy = null;

    /** 転出済 */
    private boolean transferred = false;
    
    /** 削除済 */
    private boolean deleted = false;
    
    @OneToMany
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private List<StudentYear> studentsYear;
    
    @OneToMany
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private List<Karte> karte;
    
    @OneToMany
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private List<Attendance> attendance;
    
    @OneToMany
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private List<Grade> grades;

}
