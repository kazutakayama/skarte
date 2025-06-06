package com.example.skarte.form;

import java.util.List;

import com.example.skarte.entity.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false) // EntityBase

public class GradeForm extends EntityBase { // EntityBase {
    /** ID */
    private Long gradeId;

    private String studentId;

    /** 年度 */
    private int year;

    /** 学期 */
    private int term;

    /** 教科 */
    private int subject;

    /** 評定 */
    private int rating;


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

    private List<Integer> years;

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    private List<Integer> terms;

    public List<Integer> getTerms() {
        return terms;
    }

    public void setTerms(List<Integer> terms) {
        this.terms = terms;
    }

    private List<Integer> subjects;

    public List<Integer> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Integer> subjects) {
        this.subjects = subjects;
    }

    private List<Integer> ratings;

    public List<Integer> getRatings() {
        return ratings;
    }

    public void setRatings(List<Integer> ratings) {
        this.ratings = ratings;
    }

}