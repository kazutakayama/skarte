package com.example.skarte.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students_year")
@Data
@EqualsAndHashCode(callSuper = false) // EntityBase
@Builder // CSV
@NoArgsConstructor // CSV
@AllArgsConstructor // CSV

public class StudentYear extends EntityBase { // EntityBase

    /** ID */
    @Id
    @Column
    @SequenceGenerator(name = "studentsYear_studentYearId_seq", sequenceName = "studentsYear_studentYearId_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentsYear_studentYearId_seq")
    private Long studentYearId = null;

    public void setId(Long studentYearId) {
        this.studentYearId = null;
    }

    @Column
    private String studentId = null;
    @ManyToOne
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student students;

    /** 年度 */
    @Column
    private Long year = null;

    /** 年 */
    @Column
    private Long nen = null;


    public Long getNen() {
        return nen;
    }

    public void setNens(Long nen) {
        this.nen = nen;
    }

    /** 組 */
    @Column
    private Long kumi = null;

    /** 番 */
    @Column
    private Long ban = null;

    /** 画像 */
    @Column
    private byte[] image = null;


}
