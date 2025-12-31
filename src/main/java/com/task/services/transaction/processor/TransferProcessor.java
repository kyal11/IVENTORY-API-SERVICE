package com.task.services.transaction.processor;

import com.task.constant.TransactionType;
import com.task.dto.itemTransaction.CreateItemTransactionReq;
import com.task.dto.itemTransaction.ItemTransactionRes;
import com.task.entity.ItemOwnerStocks;
import com.task.entity.Items;
import com.task.entity.ItemsTransactions;
import com.task.entity.Users;
import com.task.exception.BadRequestException;
import com.task.exception.NotFoundException;
import com.task.mapper.ItemTransactionMapper;
import com.task.repository.*;
import com.task.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferProcessor implements  TransactionProcessor{
    private final  ItemsRepository itemsRepository;
    private final OwnersRepository ownersRepository;
    private final ItemOwnerStocksRepository itemOwnerStocksRepository;
    private final ItemsTransactionsRepository transactionsRepository;
    private final UsersRepository usersRepository;

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

        ItemsTransactions tx = new ItemsTransactions();
        tx.setItem(item);
        tx.setTransactionType(TransactionType.TRANSFER);
        tx.setFromOwnerId(request.getFromOwnerId());
        tx.setToOwnerId(request.getToOwnerId());
        tx.setQuantity(transferQty);
        tx.setNotes(request.getNotes());
        Users currentUser = usersRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        tx.setPerformedBy(currentUser);

        ItemsTransactions savedTx = transactionsRepository.save(tx);

        return mapper.toItemTransactionRes(savedTx);
    }
}
