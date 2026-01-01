package com.task.inventory.services.transaction.processor;

import com.task.inventory.constant.LoanStatus;
import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import com.task.inventory.entity.ItemOwnerStocks;
import com.task.inventory.entity.ItemTransactions;
import com.task.inventory.entity.Items;
import com.task.inventory.entity.Users;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemTransactionMapper;
import com.task.inventory.repository.*;
import com.task.inventory.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferProcessor implements  TransactionProcessor{
    private final ItemsRepository itemsRepository;
    private final OwnersRepository ownersRepository;
    private final ItemOwnerStocksRepository itemOwnerStocksRepository;
    private final ItemsTransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;
    private final ItemLoanRepository itemLoanRepository;

    private final ItemTransactionMapper mapper;

    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

    @Override
    public ItemTransactionRes process(CreateItemTransactionReq request) {
        Items item = itemsRepository.findById(request.getItemId())
                .orElseThrow(() -> new NotFoundException("User with ID " + request.getItemId() + " not found"));

        ownersRepository.findById(request.getFromOwnerId())
                .orElseThrow(() -> new NotFoundException("From owner not found"));

        ownersRepository.findById(request.getToOwnerId())
                .orElseThrow(() -> new NotFoundException("To owner not found"));

        boolean hasActiveLoan = itemLoanRepository
                .existsByItemIdAndStatus(item.getId(), LoanStatus.BORROWED);

        if (hasActiveLoan) {
            throw new BadRequestException(
                    "Item cannot be transferred because it is currently borrowed"
            );
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        ItemOwnerStocks fromStock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(item.getId(), request.getFromOwnerId())
                .orElseThrow(() ->
                        new BadRequestException("Source owner does not have this item")
                );


        int fromQty = fromStock.getQuantity();
        int transferQty = request.getQuantity();

        if (fromQty < transferQty) {
            throw new BadRequestException("Insufficient stock to transfer");
        }

        if (fromQty == transferQty) {
            itemOwnerStocksRepository.delete(fromStock);
        } else {
            fromStock.setQuantity(fromQty - transferQty);
            itemOwnerStocksRepository.save(fromStock);
        }

        ItemOwnerStocks toStock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(item.getId(), request.getToOwnerId())
                .orElse(null);

        if (toStock == null) {
            toStock = new ItemOwnerStocks();
            toStock.setItem(item);
            toStock.setOwnerId(request.getToOwnerId());
            toStock.setQuantity(transferQty);
        } else {
            toStock.setQuantity(toStock.getQuantity() + transferQty);
        }

        itemOwnerStocksRepository.save(toStock);

        Users currentUser = usersRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemTransactions tx = new ItemTransactions();
        tx.setItem(item);
        tx.setTransactionType(TransactionType.TRANSFER);
        tx.setFromOwnerId(request.getFromOwnerId());
        tx.setToOwnerId(request.getToOwnerId());
        tx.setQuantity(transferQty);
        tx.setNotes(request.getNotes());
        tx.setPerformedBy(currentUser);
        tx.setCreatedAt(LocalDateTime.now());
        tx.setUpdatedAt(LocalDateTime.now());

        ItemTransactions savedTx = transactionsRepository.save(tx);

        return mapper.toItemTransactionRes(savedTx);
    }
}
