package com.example.skarte.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = false) // EntityBase
@Builder // CSV
@NoArgsConstructor // CSV
@AllArgsConstructor // CSV

public class Student extends EntityBase { // EntityBase

    /** ID */
    @Id
    @Column
    private String studentId = null;

    /** 姓 */
    @Column(length = 20, nullable = false)
    @NotEmpty
    private String lastName = null;

    /** 名 */
    @Column(length = 20, nullable = false)
    @NotEmpty
    private String firstName = null;

    /** せい */
    @Column(length = 20, nullable = false)
    @NotEmpty
    private String lastNameKana = null;

    /** めい */
    @Column(length = 20, nullable = false)
    @NotEmpty
    private String firstNameKana = null;

    /** 生年月日 */
    @Column
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth = null;

    /** 性別 */
    @Column
    @NotNull
    private Integer gender = null;

    /** 保護者1 */
    @Column
    private String family1 = null;

    /** 保護者2 */
    @Column
    private String family2 = null;

    /** 電話1 */
    @Column
    private String tel1 = null;

    /** 電話2 */
    @Column
    private String tel2 = null;

    /** 電話3 */
    @Column
    private String tel3 = null;

    /** 電話4 */
    @Column
    private String tel4 = null;

    /** 郵便番号 */
    @Column
    private String postalCode = null;

    /** 住所 */
    @Column
    private String adress = null;

    /** メモ */
    @Column
    private String memo = null;

    /** 転出・卒業 */
    private boolean transferred = false;
    
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
    private List<Grade> grade;

}
