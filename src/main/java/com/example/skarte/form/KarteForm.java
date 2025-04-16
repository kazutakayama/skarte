package com.example.skarte.form;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.skarte.entity.EntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

@EqualsAndHashCode(callSuper = false) // EntityBase

public class KarteForm extends EntityBase {

    /** ID */
    private Long karteId = null;

    private String studentId = null;

    /** 日付 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date = null;

    /** 内容 */
    private String contents = null;

}
