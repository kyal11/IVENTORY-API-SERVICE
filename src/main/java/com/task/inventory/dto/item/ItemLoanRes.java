package com.task.inventory.dto.item;

import com.task.inventory.constant.LoanStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ItemLoanRes {
    private UUID loanId;
    private String borrowedBy;
    private String performedBy;
    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;
    private LoanStatus status;
}

