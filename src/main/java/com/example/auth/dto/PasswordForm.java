package com.example.auth.dto;
import jakarta.validation.constraints.*;
public class PasswordForm {
@NotBlank @Size(min=8,max=72) public String password;
@NotBlank public String confirmPassword;
}
