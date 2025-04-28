package com.example.skarte.form;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.skarte.entity.EntityBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

@EqualsAndHashCode(callSuper = false) // EntityBase

public class KarteForm extends EntityBase {

    /** ID */
    private Long karteId;

    private String studentId;

    /** 日付 */
    @NotNull(message = "「日付」を入力してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /** 内容 */
    @NotBlank(message = "「内容」を入力してください")
    @Size(max = 1000, message = "「内容」は1000文字以内で入力してください")
    private String contents;

}
