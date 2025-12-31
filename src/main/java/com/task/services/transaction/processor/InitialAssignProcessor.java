package com.task.services.transaction.processor;

import com.task.constant.TransactionType;
import com.task.dto.ApiResponse;
import com.task.dto.item.ItemRes;
import com.task.dto.itemTransaction.CreateItemTransactionReq;
import com.task.dto.itemTransaction.ItemTransactionRes;
import com.task.entity.Items;
import com.task.entity.ItemsTransactions;
import com.task.entity.Users;
import com.task.exception.BadRequestException;
import com.task.exception.NotFoundException;
import com.task.mapper.ItemTransactionMapper;
import com.task.repository.ItemsRepository;
import com.task.repository.ItemsTransactionsRepository;
import com.task.repository.UsersRepository;
import com.task.security.SecurityUtils;
import com.task.services.ItemsService;
import com.task.services.transaction.ItemTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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


        ItemsTransactions tx = new ItemsTransactions();
        tx.setItem(itemEntity);
        tx.setTransactionType(TransactionType.INITIAL_ASSIGN);
        tx.setQuantity(itemEntity.getTotalQuantity());
        tx.setNotes(request.getNotes());
        Users currentUser = usersRepository.findById(SecurityUtils.getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        tx.setPerformedBy(currentUser);
        tx.setFromOwnerId(null);
        tx.setToOwnerId(null);

        ItemsTransactions savedTx = transactionsRepository.save(tx);
        return mapper.toItemTransactionRes(savedTx);
    }
}
