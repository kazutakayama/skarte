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
import jakarta.persistence.Id;
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
@Builder // CSV
@NoArgsConstructor // CSV
@AllArgsConstructor // CSV
@EqualsAndHashCode(callSuper = false) // EntityBase
public class StudentForm extends EntityBase { // EntityBase

    /** ID */
//    @NotEmpty
    private String studentId = null;

    /** 姓 */
    @NotEmpty
    private String lastName = null;

    /** 名 */
    @NotEmpty
    private String firstName = null;

    /** せい */
    @NotEmpty
    private String lastNameKana = null;

    /** めい */
    @NotEmpty
    private String firstNameKana = null;

    /** 生年月日 */
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth = null;

    /** 性別 */
    @NotNull
    private Integer gender = null;

    /** 家族1 */
    private String family1 = null;

    /** 家族2 */
    private String family2 = null;

    /** 電話1 */
    private String tel1 = null;

    /** 電話2 */
    private String tel2 = null;

    /** 電話3 */
    private String tel3 = null;

    /** 電話4 */
    private String tel4 = null;

    /** 郵便番号 */
    private String postalCode = null;

    /** 住所 */
    private String adress = null;

    /** メモ */
    private String memo = null;

    /** 転出 */
    private boolean transferred = false;

    // リスト
    private List<String> studentIds;

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }
    
    private List<String> lastNames;

    public List<String> getLastNames() {
        return lastNames;
    }

    public void setLastNames(List<String> lastNames) {
        this.lastNames = lastNames;
    }
    
    private List<String> firstNames;

    public List<String> getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(List<String> firstNames) {
        this.firstNames = firstNames;
    }
    
    private List<String> lastNameKanas;

    public List<String> getLastNameKanas() {
        return lastNameKanas;
    }

    public void setLastNameKanas(List<String> lastNameKanas) {
        this.lastNameKanas = lastNameKanas;
    }
    
    private List<String> firstNameKanas;

    public List<String> getFirstNameKanas() {
        return firstNameKanas;
    }

    public void setFirstNameKanas(List<String> firstNameKanas) {
        this.firstNameKanas = firstNameKanas;
    }


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private List<Date> births;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public List<Date> getBirths() {
        return births;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBirths(List<Date> births) {
        this.births = births;
    }

    private List<Integer> genders;

    public List<Integer> getGenders() {
        return genders;
    }

    public void setGenders(List<Integer> genders) {
        this.genders = genders;
    }
    
    private List<String> family1s;

    public List<String> getFamily1s() {
        return family1s;
    }

    public void setFamily1s(List<String> family1s) {
        this.family1s = family1s;
    }
    
    private List<String> family2s;

    public List<String> getFamily2s() {
        return family2s;
    }

    public void setFamily2s(List<String> family2s) {
        this.family2s = family2s;
    }

    private List<String> tel1s;

    public List<String> getTel1s() {
        return tel1s;
    }

    public void setTel1s(List<String> tel1s) {
        this.tel1s = tel1s;
    }

    private List<String> tel2s;

    public List<String> getTel2s() {
        return tel2s;
    }

    public void setTel2s(List<String> tel2s) {
        this.tel2s = tel2s;
    }
    
    private List<String> tel3s;

    public List<String> getTel3s() {
        return tel3s;
    }

    public void setTel3s(List<String> tel3s) {
        this.tel3s = tel3s;
    }

    private List<String> tel4s;

    public List<String> getTel4s() {
        return tel4s;
    }

    public void setTel4s(List<String> tel4s) {
        this.tel4s = tel4s;
    }
    
    private List<String> postalCodes;

    public List<String> getPostalCodes() {
        return postalCodes;
    }

    public void setPostalCodes(List<String> postalCodes) {
        this.postalCodes = postalCodes;
    }
    
    private List<String> adresses;

    public List<String> getAdresses() {
        return adresses;
    }

    public void setAdresses(List<String> adresses) {
        this.adresses = adresses;
    }
    
    private List<String> memos;

    public List<String> getMemos() {
        return memos;
    }

    public void setMemos(List<String> memos) {
        this.memos = memos;
    }
}
