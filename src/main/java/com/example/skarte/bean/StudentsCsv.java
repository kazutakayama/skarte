package com.example.skarte.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Value;


@Value
@AllArgsConstructor
@JsonPropertyOrder({ "生徒ID", "姓", "名" })
public class StudentsCsv {
    @JsonProperty("生徒ID")
    private Long studentId;
    @JsonProperty("姓")
    private String lastName;
    @JsonProperty("名")
    private String firstName;    
    @JsonProperty("せい")
    private String lastNameKana;
    @JsonProperty("めい")
    private String firstNameKana;
    
    @JsonProperty("保護者名")
    private String family1;
    @JsonProperty("電話")
    private Long tel1;
    @JsonProperty("郵便番号")
    private Long postalCode;
    @JsonProperty("住所")
    private String adress;
    @JsonProperty("メモ")
    private String memo;
    

}
