package com.example.skarte.form;

import java.util.List;

import com.example.skarte.entity.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false) // EntityBase

public class GradeForm extends EntityBase { // EntityBase {
    /** ID */
    private Long gradeId = null;

    private String studentId = null;

    /** 年度 */
    private Long year = null;

    /** 学期 */
    private Long term = null;

    /** 教科 */
    private Long subject = null;

    /** 評定 */
    private Long rating = null;


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