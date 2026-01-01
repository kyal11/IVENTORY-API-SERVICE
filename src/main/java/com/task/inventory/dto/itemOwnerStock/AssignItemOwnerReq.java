package com.task.inventory.dto.itemOwnerStock;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class AssignItemOwnerReq {
    private UUID itemId;

    @NotBlank
    private UUID ownerId;

    private Integer quantity;
}

