package com.example.skarte.form;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.skarte.entity.EntityBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Pattern(regexp="^[0-9]{7}$", message = "「生徒ID」は半角数字7桁で入力してください")
    private String studentId;

    /** 姓 */
    @NotBlank(message = "「姓」を入力してください")
    @Size(max=20, message = "「姓」は20字文字以内で入力してください")
    private String lastName;

    /** 名 */
    @NotBlank(message = "「名」を入力してください")
    @Size(max=20, message = "「名」は20文字以内で入力してください")
    private String firstName;

    /** せい */
    @NotBlank(message = "「せい」を入力してください")
    @Pattern(regexp="^[\u3040-\u309F]{0,20}$", message = "「せい」はひらがな20文字以内で入力してください")
    private String lastNameKana;

    /** めい */
    @NotBlank(message = "「めい」を入力してください")
    @Pattern(regexp="^[\u3040-\u309F]{0,20}$", message = "「めい」はひらがな20文字以内で入力してください")
    private String firstNameKana;

    /** 生年月日 */
    @NotNull(message = "「生年月日」を入力してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    /** 性別 */
    @NotNull(message = "「性別」を入力してください")
    @Range(min = 1, max = 3, message = "1:男,2:女,3:他のいずれかの半角数字を入力してください")
    private Integer gender;

    /** 保護者1 */
    @Size(max=30, message = "「保護者名」は30文字以内で入力してください")
    private String family1;

    /** 保護者2 */
    @Size(max=30, message = "「保護者名」は30文字以内で入力してください")
    private String family2;

    /** 電話1 */
    @Pattern(regexp="^[0-9-]{0,13}$", message = "「電話番号」は13桁以内で入力してください※半角数字,ハイフン(-)が使用できます")
    private String tel1;

    /** 電話2 */
    @Pattern(regexp="^[0-9-]{0,13}$", message = "「電話番号」は13桁以内で入力してください※半角数字,ハイフン(-)が使用できます")
    private String tel2;

    /** 電話3 */
    @Pattern(regexp="^[0-9-]{0,13}$", message = "「電話番号」は13桁以内で入力してください※半角数字,ハイフン(-)が使用できます")
    private String tel3;

    /** 電話4 */
    @Pattern(regexp="^[0-9-]{0,13}$", message = "「電話番号」は13桁以内で入力してください※半角数字,ハイフン(-)が使用できます")
    private String tel4;

    /** 郵便番号 */
    @Pattern(regexp="^[0-9-]{0,8}$", message = "「郵便番号」は8桁以内で入力してください※半角数字,ハイフン(-)が使用できます")
    private String postalCode;

    /** 住所 */
    @Size(max=100, message = "「住所」は100文字以内で入力してください")
    private String adress;

    /** メモ */
    @Size(max=500, message = "「メモ」は500文字以内で入力してください")
    private String memo;

    /** 転出・卒業 */
    private boolean transferred;

    
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
    private List<LocalDate> births;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public List<LocalDate> getBirths() {
        return births;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBirths(List<LocalDate> births) {
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
