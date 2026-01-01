package com.task.inventory.dto.user;

import com.task.inventory.constant.UserRole;
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