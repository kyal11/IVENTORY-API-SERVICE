package com.task.inventory.dto.user;

import com.task.inventory.constant.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
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
