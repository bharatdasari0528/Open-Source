package com.example.auth.dto;
import jakarta.validation.constraints.*;
public class RegisterForm {
@NotBlank @Size(min=2,max=80) public String name;
@NotBlank @Email public String email;
@NotBlank @Size(min=8,max=72) public String password;
@NotBlank public String confirmPassword;
}
