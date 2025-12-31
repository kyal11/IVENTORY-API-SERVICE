package com.task.dto.user;

import com.task.constant.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserReq {

    private String name;

    @Email
    private String email;

    private UserRole userRole;
    
    @Min(8)
    private String password;
}
