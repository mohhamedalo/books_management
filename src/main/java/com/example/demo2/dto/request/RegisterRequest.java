package com.example.demo2.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @Email(message = "Email không hợp lệ")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, message = "Mật khẩu phải ít nhất 6 ký tự")
    private String password;
}