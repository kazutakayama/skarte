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
    private Long studentId = null;

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
    @NotEmpty
    private String family1 = null;

    /** 家族2 */
    private String family2 = null;

    /** 家族3 */
    private String family3 = null;

    /** 家族4 */
    private String family4 = null;

    /** 電話1 */
    @NotNull
    private Long tel1 = null;

    /** 電話2 */
    private Long tel2 = null;

    /** 電話3 */
    private Long tel3 = null;

    /** 電話4 */
    private Long tel4 = null;

    /** 郵便番号 */
    @NotNull
    private Long postalCode = null;

    /** 住所 */
    @NotEmpty
    private String adress = null;

    /** メモ */
    private String memo = null;
}
