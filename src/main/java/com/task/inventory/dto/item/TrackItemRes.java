package com.task.inventory.dto.item;

import com.task.inventory.constant.ItemStatus;
import com.task.inventory.dto.itemOwnerStock.ItemOwnerStockRes;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class TrackItemRes {
    private ItemRes itemDataDetail;

    private List<ItemLoanRes> itemLoanHistoryResList;

    private List<ItemLoanRes> activeBorrowed;

    private List<ItemTransactionRes> historyTransaction;
}
