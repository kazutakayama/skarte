package com.example.skarte.form;

import com.example.skarte.entity.EntityBase;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeForm  extends EntityBase{

    /** ID */
    private Long noticeId;
    
    /** 内容 */
    @NotBlank(message = "「内容」を入力してください")
    @Size(max = 1000, message = "「内容」は1000文字以内で入力してください")
    private String contents;
    
}
