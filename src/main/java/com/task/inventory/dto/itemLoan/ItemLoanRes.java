package com.task.inventory.dto.itemLoan;

import com.task.inventory.constant.LoanStatus;
import com.task.inventory.entity.Owners;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ItemLoanRes {
    private UUID loanId;
    private Owners ownerId;
    private Owners borrowerId;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private LoanStatus status;
}

