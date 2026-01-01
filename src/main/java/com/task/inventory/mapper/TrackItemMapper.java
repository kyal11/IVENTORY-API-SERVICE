package com.task.inventory.mapper;

import com.task.inventory.dto.item.ItemLoanRes;
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
                        .map(this::toItemLoanRes)
                        .collect(Collectors.toList())
        );
        res.setActiveBorrowed(
                activeBorrowed.stream()
                        .map(this::toItemLoanRes)
                        .collect(Collectors.toList())
        );

        res.setHistoryTransaction(
                transactionHistory.stream()
                        .map(itemTransactionMapper::toItemTransactionRes)
                        .collect(Collectors.toList())
        );

        return res;
    }
    private ItemLoanRes toItemLoanRes(ItemLoan loan) {
        ItemLoanRes res = new ItemLoanRes();
        res.setLoanId(loan.getId());
        res.setPerformedBy(loan.getBorrowerId().toString());
        res.setBorrowedAt(loan.getBorrowedAt());
        res.setReturnedAt(loan.getReturnedAt());
        res.setStatus(loan.getStatus());
        return res;
    }
}
