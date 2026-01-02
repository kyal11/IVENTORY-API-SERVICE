package com.task.inventory.mapper;

import com.task.inventory.dto.itemLoan.ItemLoanRes;
import com.task.inventory.entity.ItemLoan;
import org.springframework.stereotype.Component;

@Component
public class ItemLoanMapper {
    public ItemLoanRes toItemLoanRes(ItemLoan loan) {
        ItemLoanRes res = new ItemLoanRes();
        res.setLoanId(loan.getId());
        res.setOwnerId(loan.getOwner());
        res.setBorrowerId(loan.getBorrower());
        res.setBorrowedAt(loan.getBorrowedAt());
        res.setReturnedAt(loan.getReturnedAt());
        res.setStatus(loan.getStatus());
        return res;
    }
}
