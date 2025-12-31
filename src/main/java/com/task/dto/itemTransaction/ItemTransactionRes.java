package com.task.dto.itemTransaction;

import com.task.constant.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ItemTransactionRes {

    private UUID id;

    private UUID itemId;
    private String itemCode;
    private String itemName;

    private UUID fromOwnerId;
    private UUID toOwnerId;

    private TransactionType transactionType;
    private Integer quantity;

    private UUID performedByUserId;
    private String performedByName;

    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

