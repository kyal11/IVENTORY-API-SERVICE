package com.task.inventory.services;

import com.task.inventory.constant.LoanStatus;
import com.task.inventory.dto.itemLoan.CreateLoanReq;
import com.task.inventory.dto.itemLoan.ReturnLoanReq;
import com.task.inventory.entity.ItemLoan;
import com.task.inventory.entity.ItemOwnerStocks;
import com.task.inventory.entity.Items;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.repository.ItemLoanRepository;
import com.task.inventory.repository.ItemOwnerStocksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemLoanService {

    private final ItemLoanRepository itemLoanRepository;
    private final ItemOwnerStocksRepository itemOwnerStocksRepository;

    public ItemLoan createLoan(
            CreateLoanReq dto
    ) {
        ItemLoan loan = new ItemLoan();
        loan.setItem(dto.getItem());
        loan.setOwner(dto.getOwnerId());
        loan.setBorrower(dto.getBorrowerId());
        loan.setQuantity(dto.getQuantity());
        loan.setStatus(LoanStatus.BORROWED);
        loan.setBorrowedAt(LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = now.plusDays(dto.getBorrowDurationDays());
        loan.setBorrowTransaction(dto.getBorrowTransaction());
        loan.setDueDate(dueDate);

        return itemLoanRepository.save(loan);
    }

    public ItemLoan returnLoan(ReturnLoanReq dto) {
        ItemLoan loan = itemLoanRepository.findByIdAndStatus(dto.getLoanId(), LoanStatus.BORROWED)
                .orElseThrow(() -> new BadRequestException("Active loan record not found or already returned"));

        ItemOwnerStocks ownerStock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(loan.getItem().getId(), loan.getOwner().getId())
                .orElseThrow(() -> new BadRequestException("Owner stock record integrity error"));

        ownerStock.setBorrowedQuantity(ownerStock.getBorrowedQuantity() - loan.getQuantity());
        itemOwnerStocksRepository.save(ownerStock);

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(LocalDateTime.now());
        loan.setReturnTransaction(dto.getReturnTransaction());
        return itemLoanRepository.save(loan);
    }
}

