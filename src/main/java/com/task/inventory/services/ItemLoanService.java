package com.task.inventory.services;

import com.task.inventory.constant.LoanStatus;
import com.task.inventory.entity.ItemLoan;
import com.task.inventory.entity.Items;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.repository.ItemLoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemLoanService {

    private final ItemLoanRepository itemLoanRepository;

    public Integer getBorrowedQuantity(UUID itemId, UUID ownerId) {
        return itemLoanRepository
                .sumBorrowedQuantity(itemId, ownerId);
    }

    public void validateAvailableStock(
            int totalStock,
            int requestedQty,
            UUID itemId,
            UUID ownerId
    ) {
        int borrowed = getBorrowedQuantity(itemId, ownerId);
        int available = totalStock - borrowed;

        if (requestedQty > available) {
            throw new BadRequestException("Owner stock not sufficient");
        }
    }

    public ItemLoan createLoan(
            Items item,
            UUID ownerId,
            UUID borrowerId,
            Integer quantity,
            Integer borrowDurationDays
    ) {
        ItemLoan loan = new ItemLoan();
        loan.setItem(item);
        loan.setOwnerId(ownerId);
        loan.setBorrowerId(borrowerId);
        loan.setQuantity(quantity);
        loan.setStatus(LoanStatus.BORROWED);
        loan.setBorrowedAt(LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dueDate = now.plusDays(borrowDurationDays);
        loan.setDueDate(dueDate);

        return itemLoanRepository.save(loan);
    }

    public ItemLoan returnLoan(UUID loanId) {
        ItemLoan loan = itemLoanRepository.findByIdAndStatus(
                loanId,
                LoanStatus.BORROWED
        ).orElseThrow(() ->
                new BadRequestException("Loan not active")
        );

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(LocalDateTime.now());

        return itemLoanRepository.save(loan);
    }
}

