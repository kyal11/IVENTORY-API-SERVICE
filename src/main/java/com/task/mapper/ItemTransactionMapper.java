package com.task.mapper;

import com.task.dto.itemTransaction.ItemTransactionRes;
import com.task.entity.ItemsTransactions;
import org.springframework.stereotype.Component;

@Component
public class ItemTransactionMapper {
    public ItemTransactionRes toItemTransactionRes(ItemsTransactions tx) {
        ItemTransactionRes res = new ItemTransactionRes();

        res.setId(tx.getId());
        res.setTransactionType(tx.getTransactionType());
        res.setQuantity(tx.getQuantity());
        res.setNotes(tx.getNotes());
        res.setCreatedAt(tx.getCreatedAt());
        res.setUpdatedAt(tx.getUpdatedAt());

        if (tx.getItem() != null) {
            res.setItemId(tx.getItem().getId());
            res.setItemCode(tx.getItem().getCodeProduct());
            res.setItemName(tx.getItem().getName());
        }
        res.setFromOwnerId(tx.getFromOwnerId());

        res.setToOwnerId(tx.getToOwnerId());

        if (tx.getPerformedBy() != null) {
            res.setPerformedByUserId(tx.getPerformedBy().getId());
            res.setPerformedByName(tx.getPerformedBy().getName());
        }

        return res;
    }
}
