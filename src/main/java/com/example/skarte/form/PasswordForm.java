package com.example.skarte.form;

import com.example.skarte.validation.constraints.PasswordEquals;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@PasswordEquals
public class PasswordForm {
    @NotBlank(message = "「現在のパスワード」を入力してください")
    private String currentPassword;

    @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,24}$", message = "「パスワード」は大文字アルファベット、小文字アルファベット、数字をすべて含む8~24文字で入力してください")
    private String password;

    @NotBlank(message = "確認のためもう一度「新しいパスワード」を入力してください")
    private String passwordConfirmation;
}
