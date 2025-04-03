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

public class GradeForm extends EntityBase { // EntityBase {
    /** ID */
//    @Id
//    @SequenceGenerator(name = "studentsYear_studentYearId_seq", sequenceName = "studentsYear_studentYearId_seq", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "studentsYear_studentYearId_seq")
    private Long gradeId = null;
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

    /** 学期 */
    private Long term = null;

    /** 教科 */
    private Long subject = null;

    /** 評定 */
    private Long rating = null;

    // リスト

    private List<Long> gradeIds;

    public List<Long> getGradeIds() {
        return gradeIds;
    }

    public void setGradeIds(List<Long> gradeIds) {
        this.gradeIds = gradeIds;
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

    private List<Long> terms;

    public List<Long> getTerms() {
        return terms;
    }

    public void setTerms(List<Long> terms) {
        this.terms = terms;
    }

    private List<Long> subjects;

    public List<Long> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Long> subjects) {
        this.subjects = subjects;
    }

    private List<Long> ratings;

    public List<Long> getRatings() {
        return ratings;
    }

    public void setRatings(List<Long> ratings) {
        this.ratings = ratings;
    }

}
