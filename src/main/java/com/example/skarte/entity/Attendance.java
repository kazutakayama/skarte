package com.example.skarte.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "attendance")
@Data
@EqualsAndHashCode(callSuper = false) // EntityBase
@Builder // CSV
@NoArgsConstructor // CSV
@AllArgsConstructor // CSV

public class Attendance extends EntityBase { // EntityBase

    /** ID */
    @Id
    @Column
    @SequenceGenerator(name = "attendance_attendanceId_seq", sequenceName = "attendance_attendanceId_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendance_attendanceId_seq")
    private Long attendanceId = null;

    public void setId(Long attendanceId) {
        this.attendanceId = null;
    }

    @Column
    private Long studentId = null;
    @ManyToOne
    @JoinColumn(name = "studentId", insertable = false, updatable = false)
    private Student students;

    /** 日付 */
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date = null;

    /** 遅刻 */
    @Column
    private Integer chikoku = null;
    
    /** 早退 */
    @Column
    private Integer soutai = null;
    
    /** 欠席 */
    @Column
    private Integer kesseki = null;
    
    /** 出停 */
    @Column
    private Integer syuttei = null;
    
    /** 忌引 */
    @Column
    private Integer kibiki = null;

}