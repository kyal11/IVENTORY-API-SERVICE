package com.task.services.transaction.processor;

import com.task.constant.TransactionType;
import com.task.dto.itemTransaction.CreateItemTransactionReq;
import com.task.dto.itemTransaction.ItemTransactionRes;
import com.task.entity.ItemsTransactions;

public interface TransactionProcessor {
    TransactionType getType();
    ItemTransactionRes process(CreateItemTransactionReq request);
}
