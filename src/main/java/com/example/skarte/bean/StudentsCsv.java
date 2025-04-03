package com.example.skarte.bean;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonPropertyOrder({ "生徒ID", "姓", "名", "せい", "めい", "生年月日", "性別", "保護者1", "保護者2", "電話1", "電話2", "電話3", "電話4", "郵便番号",
        "住所", "メモ" })

// CSVダウンロード用
public class StudentsCsv {

    @JsonProperty("生徒ID")
    private String studentId;
    @JsonProperty("姓")
    private String lastName;
    @JsonProperty("名")
    private String firstName;
    @JsonProperty("せい")
    private String lastNameKana;
    @JsonProperty("めい")
    private String firstNameKana;

    @JsonProperty("生年月日")
    private Date birth;

    @JsonProperty("性別")
    private Integer gender;
    
    @JsonProperty("保護者1")
    private String family1;
    @JsonProperty("保護者2")
    private String family2;

    @JsonProperty("電話1")
    private String tel1;
    @JsonProperty("電話2")
    private String tel2;
    @JsonProperty("電話3")
    private String tel3;
    @JsonProperty("電話4")
    private String tel4;

    @JsonProperty("郵便番号")
    private String postalCode;
    @JsonProperty("住所")
    private String adress;
    @JsonProperty("メモ")
    private String memo;

}
