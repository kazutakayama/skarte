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
    private Long studentYearId = null;

    private String studentId = null;

    /** 年度 */
    private Long year = null;

    /** 年 */
    private Long nen = null;

    /** 組 */
    private Long kumi = null;

    /** 番 */
    private Long ban = null;

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
