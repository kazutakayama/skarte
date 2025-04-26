package com.example.skarte.form;

import com.example.skarte.validation.constraints.PasswordEquals;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordEquals
public class UserForm {

    @Pattern(regexp="^[0-9]{7}$", message = "「ユーザーID」は半角数字7桁で入力してください")
    private String userId;
    
    @NotBlank(message = "「姓」を入力してください")
    @Size(max=20, message = "「姓」は20字文字以内で入力してください")
    private String lastName;
    
    @NotBlank(message = "「名」を入力してください")
    @Size(max=20, message = "「名」は20文字以内で入力してください")
    private String firstName;

    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,24}$", message = "「パスワード」は大文字アルファベット、小文字アルファベット、数字をすべて含む8~24文字で入力してください")
    private String password;

    @NotEmpty(message = "確認のためもう一度「パスワード」を入力してください")
    private String passwordConfirmation;

}