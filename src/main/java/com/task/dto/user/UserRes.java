package com.task.dto.user;

import com.task.constant.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserRes {
    private UUID id;
    private String name;
    private String email;
    private UserRole userRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}