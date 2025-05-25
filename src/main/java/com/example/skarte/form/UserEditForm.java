package com.example.skarte.form;

import com.example.skarte.entity.User.Authority;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

//教師情報を編集する用のフォーム
@Data
public class UserEditForm {
    
    @Pattern(regexp="^[0-9]{7}$", message = "「ユーザーID」は半角数字7桁で入力してください")
    private String userId;
    
    @NotBlank(message = "「姓」を入力してください")
    @Size(max=20, message = "「姓」は20字文字以内で入力してください")
    private String lastName;
    
    @NotBlank(message = "「名」を入力してください")
    @Size(max=20, message = "「名」は20文字以内で入力してください")
    private String firstName;
    
    private Authority authority;

}
