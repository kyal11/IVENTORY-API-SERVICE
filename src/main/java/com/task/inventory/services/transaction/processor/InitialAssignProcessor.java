package com.task.inventory.services.transaction.processor;

import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.item.ItemRes;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import com.task.inventory.entity.Items;
import com.task.inventory.entity.ItemTransactions;
import com.task.inventory.entity.Users;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemTransactionMapper;
import com.task.inventory.repository.ItemsRepository;
import com.task.inventory.repository.ItemsTransactionsRepository;
import com.task.inventory.repository.UsersRepository;
import com.task.inventory.security.SecurityUtils;
import com.task.inventory.services.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InitialAssignProcessor implements TransactionProcessor {
    private final ItemsService itemsService;
    private  final ItemsTransactionsRepository transactionsRepository;
    private final ItemsRepository itemsRepository;
    private final ItemTransactionMapper mapper;
    private final UsersRepository usersRepository;

    @Override
    public TransactionType getType() {
        return TransactionType.INITIAL_ASSIGN;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ItemTransactionRes process(CreateItemTransactionReq request) {
        if (request.getItem() == null) {
            throw new BadRequestException("Item payload is required for INITIAL_ASSIGN");
        }

        ApiResponse<ItemRes> itemResponse = itemsService.create(request.getItem());
        ItemRes createdItemDto = itemResponse.getData();

        Items itemEntity = itemsRepository.findById(createdItemDto.getId())
                .orElseThrow(() -> new NotFoundException("Item not found after creation"));

        Users currentUser = usersRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        ItemTransactions tx = new ItemTransactions();
        tx.setItem(itemEntity);
        tx.setTransactionType(TransactionType.INITIAL_ASSIGN);
        tx.setQuantity(itemEntity.getTotalQuantity());
        tx.setNotes(request.getNotes());
        tx.setPerformedBy(currentUser);
        tx.setFromOwnerId(null);
        tx.setToOwnerId(null);
        tx.setCreatedAt(LocalDateTime.now());
        tx.setUpdatedAt(LocalDateTime.now());

        ItemTransactions savedTx = transactionsRepository.save(tx);
        return mapper.toItemTransactionRes(savedTx);
    }
}
