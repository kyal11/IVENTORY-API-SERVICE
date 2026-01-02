package com.task.inventory.dto.item;

import com.task.inventory.dto.itemLoan.ItemLoanRes;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import lombok.Data;

import java.util.List;

@Data
public class TrackItemRes {
    private ItemRes itemDataDetail;

    private List<ItemLoanRes> itemLoanHistoryResList;

    private List<ItemLoanRes> activeBorrowed;

    private List<ItemTransactionRes> historyTransaction;
}
