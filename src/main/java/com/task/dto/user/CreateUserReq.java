package com.task.dto.user;

import com.task.constant.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserReq {
    private UUID id;
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;

    @NotBlank
    private UserRole userRole;

    @NotBlank
    @Min(8)
    private String password;
}
