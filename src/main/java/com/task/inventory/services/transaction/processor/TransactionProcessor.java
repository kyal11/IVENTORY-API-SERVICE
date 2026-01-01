package com.task.inventory.services.transaction.processor;

import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;

public interface TransactionProcessor {
    TransactionType getType();
    ItemTransactionRes process(CreateItemTransactionReq request);
}
