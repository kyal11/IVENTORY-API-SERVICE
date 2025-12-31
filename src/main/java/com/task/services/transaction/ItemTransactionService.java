package com.task.services.transaction;

import com.task.constant.TransactionType;
import com.task.dto.ApiResponse;
import com.task.dto.itemTransaction.CreateItemTransactionReq;
import com.task.dto.itemTransaction.ItemTransactionRes;
import com.task.entity.ItemsTransactions;
import com.task.exception.NotFoundException;
import com.task.mapper.ItemTransactionMapper;
import com.task.repository.ItemsTransactionsRepository;
import com.task.services.transaction.processor.TransactionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemTransactionService {
    private ItemsTransactionsRepository itemsTransactionsRepository;
    private final Map<TransactionType, TransactionProcessor> processorMap;
    private final ItemTransactionMapper mapper;

    public ApiResponse<ItemTransactionRes> process(CreateItemTransactionReq dto) {
        TransactionProcessor processor = processorMap.get(dto.getTransactionType());

        if (processor == null) {
            throw new IllegalArgumentException("Unsupported transaction type");
        }
        ItemTransactionRes transactions = processor.process(dto);

        return ApiResponse.success(
                "Transaction processed successfully",
                transactions
        );
    }

    public ApiResponse<ItemTransactionRes> findById(UUID id) {
        ItemsTransactions transaction = itemsTransactionsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction with ID " + id + " not found"));

        return ApiResponse.success("Transaction detail retrieved successfully", mapper.toItemTransactionRes(transaction));
    }

    public ApiResponse<Page<ItemTransactionRes>> findAll(Pageable pageable) {
        Page<ItemTransactionRes> result = itemsTransactionsRepository.findAll(pageable)
                .map(mapper::toItemTransactionRes);

        return ApiResponse.success("All transactions retrieved successfully", result);
    }

    public ApiResponse<List<ItemTransactionRes>> findByItem(UUID itemId) {
        List<ItemTransactionRes> result = itemsTransactionsRepository.findByItemIdOrderByCreatedAtDesc(itemId)
                .stream()
                .map(mapper::toItemTransactionRes)
                .toList();

        return ApiResponse.success("Transaction history for item retrieved successfully", result);
    }

    public ApiResponse<List<ItemTransactionRes>> findByType(TransactionType type) {
        List<ItemTransactionRes> result = itemsTransactionsRepository.findByTransactionType(type)
                .stream()
                .map(mapper::toItemTransactionRes)
                .toList();

        return ApiResponse.success("Transactions filtered by type: " + type + " retrieved successfully", result);
    }

    public ApiResponse<List<ItemTransactionRes>> findByPerformedBy(UUID userId) {
        List<ItemTransactionRes> result = itemsTransactionsRepository.findByPerformedById(userId)
                .stream()
                .map(mapper::toItemTransactionRes)
                .toList();

        return ApiResponse.success("Transactions performed by user retrieved successfully", result);
    }

    public ApiResponse<List<ItemTransactionRes>> findByDateRange(
            LocalDateTime start,
            LocalDateTime end
    ) {
        List<ItemTransactionRes> result = itemsTransactionsRepository.findByCreatedAtBetween(start, end)
                .stream()
                .map(mapper::toItemTransactionRes)
                .toList();

        return ApiResponse.success("Transactions from " + start + " to " + end + " retrieved successfully", result);
    }

    public ApiResponse<Page<ItemTransactionRes>> search(
            String itemCode,
            String itemName,
            TransactionType transactionType,
            UUID performedByUserId,
            String performedByName,
            Pageable pageable
    ) {
        Page<ItemTransactionRes> page = itemsTransactionsRepository.searchTransactions(
                        itemCode,
                        itemName,
                        transactionType,
                        performedByUserId,
                        performedByName,
                        pageable
                )
                .map(mapper::toItemTransactionRes);

        return ApiResponse.success("Transaction search results retrieved successfully", page);
    }
}

