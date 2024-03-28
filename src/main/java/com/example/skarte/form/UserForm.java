package com.example.skarte.form;

import com.example.skarte.validation.constraints.PasswordEquals;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordEquals
public class UserForm {


//    @NotEmpty
    private Long userId;
    
    @NotEmpty
    private String lastName;
    
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirmation;

}