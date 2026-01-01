package com.task.inventory.dto.itemTransaction;

import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.item.CreateItemReq;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateItemTransactionReq {

    private UUID itemId;

    private UUID fromOwnerId;
    private UUID toOwnerId;

    // for assign
    private CreateItemReq item;

    @NotBlank
    private TransactionType transactionType;

    private Integer quantity;

    private String notes;

    // for return
    private UUID itemLoanId;

    //for borrow
    private Integer dayDueDate;
}

