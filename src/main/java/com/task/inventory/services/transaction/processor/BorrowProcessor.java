package com.task.inventory.services.transaction.processor;

import com.task.inventory.constant.ItemLogType;
import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.itemLoan.CreateLoanReq;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import com.task.inventory.entity.*;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemMapper;
import com.task.inventory.mapper.ItemTransactionMapper;
import com.task.inventory.repository.*;
import com.task.inventory.security.SecurityUtils;
import com.task.inventory.services.ItemLoanService;
import com.task.inventory.services.ItemLogsService;
import com.task.inventory.services.ItemsService;
import com.task.inventory.utils.ObjectToJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BorrowProcessor implements TransactionProcessor{

    private final ItemsRepository itemsRepository;
    private final ItemOwnerStocksRepository itemOwnerStocksRepository;
    private final ItemLoanService itemLoanService;
    private final ItemsService itemsService;
    private final ItemsTransactionsRepository transactionsRepository;
    private final ItemTransactionMapper mapper;
    private final UsersRepository usersRepository;
    private final OwnersRepository ownersRepository;

    private final ItemMapper itemMapper;
    private final ObjectToJson objectToJson;
    private final ItemLogsService itemLogsService;

    @Override
    public TransactionType getType() {
        return TransactionType.BORROW;
    }

    @Override
    @Transactional
    public ItemTransactionRes process(CreateItemTransactionReq request) {

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        if (request.getFromOwnerId() == null || request.getToOwnerId() == null) {
            throw new BadRequestException("Owner and borrower must be provided");
        }

        if (request.getFromOwnerId().equals(request.getToOwnerId())) {
            throw new BadRequestException("Owner and borrower cannot be the same");
        }

        if (request.getDayDueDate() == null || request.getDayDueDate() <= 0) {
            throw new BadRequestException("Invalid due date");
        }

        Owners fromOwner = ownersRepository.findById(request.getFromOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner (From) not found"));

        Owners toOwner = ownersRepository.findById(request.getToOwnerId())
                .orElseThrow(() -> new NotFoundException("Borrower (To) not found"));

        Items item = itemsRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        // Audit Item Log
        String beforeState = objectToJson.toJson(itemMapper.toItemRes(item));

        ItemOwnerStocks ownerStock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(item.getId(), request.getFromOwnerId())
                .orElseThrow(() -> new BadRequestException("Owner does not own this item"));

        int available = ownerStock.getAvailableQuantity();
        if (available < request.getQuantity()) {
            throw new BadRequestException("Insufficient available stock");
        }

        ownerStock.setBorrowedQuantity(
                ownerStock.getBorrowedQuantity() + request.getQuantity()
        );

        itemOwnerStocksRepository.save(ownerStock);

        Users currentUser = usersRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemTransactions tx = new ItemTransactions();
        tx.setItem(item);
        tx.setFromOwnerId(request.getFromOwnerId());
        tx.setToOwnerId(request.getToOwnerId());
        tx.setTransactionType(TransactionType.BORROW);
        tx.setQuantity(request.getQuantity());
        tx.setPerformedBy(currentUser);
        tx.setNotes(request.getNotes());
        tx.setCreatedAt(LocalDateTime.now());
        tx.setUpdatedAt(LocalDateTime.now());
        ItemTransactions savedTx = transactionsRepository.save(tx);

        CreateLoanReq loanReq = new CreateLoanReq();
        loanReq.setItem(item);
        loanReq.setOwnerId(fromOwner);
        loanReq.setBorrowerId(toOwner);
        loanReq.setQuantity(request.getQuantity());
        loanReq.setBorrowDurationDays(request.getDayDueDate());
        loanReq.setBorrowTransaction(savedTx);
        ItemLoan newLoanData = itemLoanService.createLoan(loanReq);
        savedTx.setBorrowLoan(newLoanData);

        itemsService.updateItemStatus(item.getId());

        // Audit Item Log
        Items itemAfterTransfer = itemsRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("User with ID " + request.getItemId() + " not found"));
        String afterState = objectToJson.toJson(itemMapper.toItemRes(itemAfterTransfer));
        itemLogsService.log(
                item.getId(),
                null,
                ItemLogType.BORROW_ITEM,
                "Transaction for borrow item",
                beforeState,
                afterState,
                SecurityUtils.getCurrentUserId()
        );
        return mapper.toItemTransactionRes(savedTx);
    }

}
