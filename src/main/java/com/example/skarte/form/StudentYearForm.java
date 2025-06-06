package com.example.skarte.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.skarte.entity.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

@EqualsAndHashCode(callSuper = false) // EntityBase

public class StudentYearForm extends EntityBase { // EntityBase {
    
    /** ID */
    private Long studentYearId;

    private String studentId;

    /** 年度 */
    private int year;

    /** 年 */
    private int nen;

    /** 組 */
    private int kumi;

    /** 番 */
    private int ban;

    /** 画像 */
    private MultipartFile image = null;

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

    private List<Integer> years;

    public List<Integer> getYears() {
        return years;
    }

    public void setYears(List<Integer> years) {
        this.years = years;
    }

    private List<Integer> nens;

    public List<Integer> getNens() {
        return nens;
    }

    public void setNens(List<Integer> nens) {
        this.nens = nens;
    }

    private List<Integer> kumis;

    public List<Integer> getKumis() {
        return kumis;
    }

    public void setKumis(List<Integer> kumis) {
        this.kumis = kumis;
    }

    private List<Integer> bans;

    public List<Integer> getBans() {
        return bans;
    }

    public void setBans(List<Integer> bans) {
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
