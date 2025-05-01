package com.example.skarte.form;

import com.example.skarte.entity.EntityBase;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeForm  extends EntityBase{

    private Long noticeId;
    
//    @NotEmpty(message = "タイトルを入力してください")
//    @NotBlank(message = "「タイトル」を入力してください")
//    @Size(max = 30, message = "「タイトル」は30文字以内で入力してください")
//    private String title;
    
    @NotBlank(message = "「内容」を入力してください")
    @Size(max = 1000, message = "「内容」は1000文字以内で入力してください")
    private String contents;
    
}
