package com.task.inventory.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRes {
    private String name;
    private String email;
    private String token;
}
