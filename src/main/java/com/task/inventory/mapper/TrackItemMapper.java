package com.task.inventory.mapper;

import com.task.inventory.dto.itemLoan.ItemLoanRes;
import com.task.inventory.dto.item.TrackItemRes;
import com.task.inventory.entity.ItemLoan;
import com.task.inventory.entity.Items;
import com.task.inventory.entity.ItemTransactions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrackItemMapper {

    private final ItemMapper itemMapper;
    private final ItemTransactionMapper itemTransactionMapper;
    private final ItemLoanMapper itemLoanMapper;

    public TrackItemRes toTrackItemRes(
            Items item,
            List<ItemLoan> loanHistory,
            List<ItemLoan> activeBorrowed,
            List<ItemTransactions> transactionHistory
    ) {
        TrackItemRes res = new TrackItemRes();
        res.setItemDataDetail(itemMapper.toItemRes(item));
        res.setItemLoanHistoryResList(
                loanHistory.stream()
                        .map(itemLoanMapper::toItemLoanRes)
                        .collect(Collectors.toList())
        );
        res.setActiveBorrowed(
                activeBorrowed.stream()
                        .map(itemLoanMapper::toItemLoanRes)
                        .collect(Collectors.toList())
        );

        res.setHistoryTransaction(
                transactionHistory.stream()
                        .map(itemTransactionMapper::toItemTransactionRes)
                        .collect(Collectors.toList())
        );

        return res;
    }
}
