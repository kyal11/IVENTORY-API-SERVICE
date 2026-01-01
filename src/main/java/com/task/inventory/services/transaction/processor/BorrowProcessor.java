package com.task.inventory.services.transaction.processor;

import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import com.task.inventory.entity.*;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemTransactionMapper;
import com.task.inventory.repository.ItemOwnerStocksRepository;
import com.task.inventory.repository.ItemsRepository;
import com.task.inventory.repository.ItemsTransactionsRepository;
import com.task.inventory.repository.UsersRepository;
import com.task.inventory.security.SecurityUtils;
import com.task.inventory.services.ItemLoanService;
import com.task.inventory.services.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Items item = itemsRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

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

        ItemLoan loan = itemLoanService.createLoan(
                item,
                request.getFromOwnerId(),
                request.getToOwnerId(),
                request.getQuantity(),
                request.getDayDueDate()
        );

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

        itemsService.updateItemStatus(item.getId());

        return mapper.toItemTransactionRes(transactionsRepository.save(tx));
    }

}
