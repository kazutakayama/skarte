package com.example.skarte.form;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.skarte.entity.Attendance;
import com.example.skarte.entity.EntityBase;
import com.example.skarte.entity.Grade;
import com.example.skarte.entity.Karte;
import com.example.skarte.entity.Student;
import com.example.skarte.entity.StudentYear;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data

@EqualsAndHashCode(callSuper = false) // EntityBase

public class StudentYearForm extends EntityBase { // EntityBase {
    /** ID */
//    @Id
//    @SequenceGenerator(name = "studentsYear_studentYearId_seq", sequenceName = "studentsYear_studentYearId_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentsYear_studentYearId_seq")
    private Long studentYearId = null;
//
//    public void setId(Long studentYearId) {
//        this.studentYearId = null;
//    }

    private String studentId = null;
//    @ManyToOne
//    @JoinColumn(name = "studentId", insertable = false, updatable = false)
//    private Student students;

    /** 年度 */
    private Long year = null;

    /** 年 */
    private Long nen = null;

    /** 組 */
    private Long kumi = null;

    /** 番 */
    private Long ban = null;

    /** 画像 */
    private String path = null;

//    /** 転出済 */
//    private boolean transferred = false;

    
    // リスト
//    private List<Long> studentYearIds;
//
//    public List<Long> getStudentYearIds() {
//        return studentYearIds;
//    }
//
//    public void setStudentYearIds(List<Long> studentYearIds) {
//        this.studentYearIds = studentYearIds;
//    }

    private List<Long> studentYearIds;

    public List<Long> getStudentYearIds() {
        return studentYearIds;
    }

    public void setStudentYearIds(List<Long> studentYearIds) {
        this.studentYearIds = studentYearIds;
    }
    
    private List<String> studentIds;

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    private List<Long> years;

    public List<Long> getYears() {
        return years;
    }

    public void setYears(List<Long> years) {
        this.years = years;
    }
    
    private List<Long> nens;

    public List<Long> getNens() {
        return nens;
    }

    public void setNens(List<Long> nens) {
        this.nens = nens;
    }
    
    private List<Long> kumis;

    public List<Long> getKumis() {
        return kumis;
    }

    public void setKumis(List<Long> kumis) {
        this.kumis = kumis;
    }
    
    private List<Long> bans;

    public List<Long> getBans() {
        return bans;
    }

    public void setBans(List<Long> bans) {
        this.bans = bans;
    }
    
    private List<String> paths;

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
    
}
