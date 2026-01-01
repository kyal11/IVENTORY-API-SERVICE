package com.task.inventory.controller;

import com.task.inventory.constant.TransactionType;
import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.itemTransaction.CreateItemTransactionReq;
import com.task.inventory.dto.itemTransaction.ItemTransactionRes;
import com.task.inventory.services.transaction.ItemTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class ItemTransactionController {
    private final ItemTransactionService transactionService;

    @PostMapping
    public ApiResponse<ItemTransactionRes> create(
            @RequestBody CreateItemTransactionReq request
    ) {
        return transactionService.process(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<ItemTransactionRes> findById(
            @PathVariable UUID id
    ) {
        return transactionService.findById(id);
    }

    @GetMapping
    public ApiResponse<Page<ItemTransactionRes>> findAll(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return transactionService.findAll(pageable);
    }


    @GetMapping("/item/{itemId}")
    public ApiResponse<List<ItemTransactionRes>> findByItem(
            @PathVariable UUID itemId
    ) {
        return transactionService.findByItem(itemId);
    }

    @GetMapping("/type/{type}")
    public ApiResponse<List<ItemTransactionRes>> findByType(
            @PathVariable TransactionType type
    ) {
        return transactionService.findByType(type);
    }

    @GetMapping("/performed-by/{userId}")
    public ApiResponse<List<ItemTransactionRes>> findByPerformedBy(
            @PathVariable UUID userId
    ) {
        return transactionService.findByPerformedBy(userId);
    }

    @GetMapping("/date-range")
    public ApiResponse<List<ItemTransactionRes>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return transactionService.findByDateRange(start, end);
    }


    @GetMapping("/search")
    public ApiResponse<Page<ItemTransactionRes>> search(
            @RequestParam(required = false) String itemCode,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) UUID performedByUserId,
            @RequestParam(required = false) String performedByName,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return transactionService.search(
                itemCode,
                itemName,
                transactionType,
                performedByUserId,
                performedByName,
                pageable
        );
    }
}
