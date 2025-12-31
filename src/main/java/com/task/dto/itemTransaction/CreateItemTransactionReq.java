package com.task.dto.itemTransaction;

import com.task.constant.TransactionType;
import com.task.dto.item.CreateItemReq;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateItemTransactionReq {

    private UUID itemId;

    private UUID fromOwnerId;
    private UUID toOwnerId;

    private CreateItemReq item;

    private TransactionType transactionType;
    private Integer quantity;
    private String notes;
}

