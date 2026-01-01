package com.task.inventory.services.transaction.processor;

import com.task.inventory.constant.LoanStatus;
import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import com.task.inventory.entity.*;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemTransactionMapper;
import com.task.inventory.repository.*;
import com.task.inventory.security.SecurityUtils;
import com.task.inventory.services.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReturnProcessor implements  TransactionProcessor{
    private final ItemLoanRepository itemLoanRepository;
    private final ItemsTransactionsRepository transactionsRepository;
    private final ItemOwnerStocksRepository itemOwnerStocksRepository;
    private final ItemTransactionMapper mapper;
    private final UsersRepository usersRepository;
    private final ItemsRepository itemsRepository;
    private final ItemsService itemsService;

    @Override
    public TransactionType getType() {
        return TransactionType.RETURN;
    }

    @Override
    @Transactional
    public ItemTransactionRes process(CreateItemTransactionReq request) {

        if (request.getItemLoanId() == null) {
            throw new BadRequestException("Item loan ID is required for return");
        }
        ItemLoan loan = itemLoanRepository.findByIdAndStatus(request.getItemLoanId(), LoanStatus.BORROWED)
                .orElseThrow(() -> new NotFoundException("Item loan not found"));

        ItemOwnerStocks ownerStock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(loan.getItem().getId(), loan.getOwnerId())
                .orElseThrow(() -> new BadRequestException("Owner does not own this item"));

        ownerStock.setBorrowedQuantity(ownerStock.getBorrowedQuantity() - loan.getQuantity());
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnedAt(LocalDateTime.now());


        itemLoanRepository.save(loan);
        itemOwnerStocksRepository.save(ownerStock);

        Users currentUser = usersRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemTransactions tx = new ItemTransactions();
        tx.setItem(loan.getItem());
        tx.setFromOwnerId(loan.getBorrowerId());
        tx.setToOwnerId(loan.getOwnerId());
        tx.setTransactionType(TransactionType.RETURN);
        tx.setQuantity(loan.getQuantity());
        tx.setPerformedBy(currentUser);
        tx.setNotes(request.getNotes());
        tx.setCreatedAt(LocalDateTime.now());
        tx.setUpdatedAt(LocalDateTime.now());

        itemsService.updateItemStatus(loan.getItem().getId());

        return mapper.toItemTransactionRes(
                transactionsRepository.save(tx)
        );
    }
}
