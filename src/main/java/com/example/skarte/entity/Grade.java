package com.example.skarte.entity;

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
@Table(name = "grade")
@Data
@EqualsAndHashCode(callSuper = false) // EntityBase
@Builder // CSV
@NoArgsConstructor // CSV
@AllArgsConstructor // CSV

public class Grade extends EntityBase { // EntityBase

    /** ID */
    @Id
    @Column
    @SequenceGenerator(name = "grade_gradeId_seq", sequenceName = "grade_gradeId_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grade_gradeId_seq")
    private Long gradeId = null;

    public void setId(Long gradeId) {
        this.gradeId = null;
    }

    @Column
    private String studentId = null;
    @ManyToOne
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student students;

    /** 年度 */
    @Column
    private Long year = null;

    /** 学期 */
    @Column
    private Long term = null;

    /** 教科 */
    @Column
    private Long subject = null;

    /** 評定 */
    @Column
    private Long rating = null;

}