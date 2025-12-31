package com.task.dto.itemOwnerStock;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ItemOwnerStockRes {

    private UUID id;

    private UUID itemId;
    private String itemCode;
    private String itemName;

    private UUID ownerId;
    private Integer quantity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

