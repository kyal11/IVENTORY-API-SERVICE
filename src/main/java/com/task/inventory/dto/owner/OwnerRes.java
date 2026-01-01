package com.task.inventory.dto.owner;

import com.task.inventory.constant.OwnerType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OwnerRes {
    private UUID id;

    private String name;

    private OwnerType type;

    private String codeOwner;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
