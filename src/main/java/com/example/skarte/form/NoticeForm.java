package com.example.skarte.form;

import com.example.skarte.entity.EntityBase;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class NoticeForm  extends EntityBase{

    private Long noticeId = null;
    
    @NotEmpty
    private String title = null;
    
    private String contents = null;
    
}
