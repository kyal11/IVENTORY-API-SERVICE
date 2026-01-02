package com.task.inventory.dto.itemLoan;

import com.task.inventory.entity.ItemTransactions;
import com.task.inventory.entity.Items;
import com.task.inventory.entity.Owners;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateLoanReq {
    private Items item;
    private Owners ownerId;
    private Owners borrowerId;
    private Integer quantity;
    private Integer borrowDurationDays;

    private ItemTransactions borrowTransaction;
}
