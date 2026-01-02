package com.task.inventory.dto.itemLoan;

import com.task.inventory.entity.ItemTransactions;
import lombok.Data;

import java.util.UUID;

@Data
public class ReturnLoanReq {
    private UUID loanId;

    private ItemTransactions returnTransaction;
}
