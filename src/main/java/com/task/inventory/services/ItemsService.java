package com.task.inventory.services;

import com.task.inventory.constant.ItemStatus;
import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.item.CreateItemReq;
import com.task.inventory.dto.item.ItemRes;
import com.task.inventory.dto.item.TrackItemRes;
import com.task.inventory.dto.item.UpdateItemReq;
import com.task.inventory.dto.itemOwnerStock.AssignItemOwnerReq;
import com.task.inventory.entity.ItemLoan;
import com.task.inventory.entity.ItemOwnerStocks;
import com.task.inventory.entity.ItemTransactions;
import com.task.inventory.entity.Items;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemMapper;
import com.task.inventory.mapper.TrackItemMapper;
import com.task.inventory.repository.ItemLoanRepository;
import com.task.inventory.repository.ItemOwnerStocksRepository;
import com.task.inventory.repository.ItemsRepository;
import com.task.inventory.repository.ItemsTransactionsRepository;
import com.task.inventory.services.transaction.ItemTransactionService;
import com.task.inventory.utils.ItemCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private final ItemsRepository itemsRepository;
    private final ItemLoanRepository itemLoanRepository;
    private final ItemsTransactionsRepository itemsTransactionsRepository;

    private final ItemOwnerStocksRepository itemOwnerStocksRepository;
    private final ItemCodeGenerator itemCodeGenerator;
    private final ItemMapper mapper;
    private final TrackItemMapper trackItemMapper;

    public ApiResponse<Page<ItemRes>> getAll(Pageable pageable) {
        Page<ItemRes> items = itemsRepository.findAll(pageable)
                .map(mapper::toItemRes);

        return ApiResponse.success("Get all items successfully", items);
    }

    public ApiResponse<ItemRes> getById(UUID id) {
        Items item = itemsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with ID " + id + " not found"));

        return ApiResponse.success("Get item successfully", mapper.toItemRes(item));
    }

    public ApiResponse<ItemRes> getByCodeProduct(String codeProduct) {
        Items item = itemsRepository.findByCodeProduct(codeProduct)
                .orElseThrow(() -> new NotFoundException("Item with code " + codeProduct + " not found"));

        return ApiResponse.success("Get item successfully", mapper.toItemRes(item));
    }

    public ApiResponse<List<ItemRes>> getByStatus(ItemStatus status) {
        List<ItemRes> items = itemsRepository.findByStatus(status)
                .stream()
                .map(mapper::toItemRes)
                .collect(Collectors.toList());

        return ApiResponse.success("Get items by status successfully", items);
    }

    public ApiResponse<TrackItemRes> getItemTrack(String codeProduct) {
        Items item = itemsRepository.findByCodeProduct(codeProduct)
                .orElseThrow(() ->
                        new NotFoundException("Item with code " + codeProduct + " not found")
                );

        String itemCodeProduct = item.getCodeProduct();

        List<ItemLoan> loanHistory =
                itemLoanRepository.findAllItemLoanHistoryByCodeProduct(itemCodeProduct);

        List<ItemLoan> activeBorrowed =
                itemLoanRepository.findActiveBorrowedByCodeProduct(itemCodeProduct);

        List<ItemTransactions> transactionHistory =
                itemsTransactionsRepository.findItemTransactionHistoryByCodeProduct(itemCodeProduct);

        TrackItemRes result = trackItemMapper.toTrackItemRes(
                item,
                loanHistory,
                activeBorrowed,
                transactionHistory
        );

        return ApiResponse.success(
                "Get item tracking data successfully",
                result
        );
    }
    public Page<ItemRes> search(
            String name, String code, ItemStatus status, Integer minQty,
            LocalDate startDate, LocalDate endDate, Pageable pageable
    ) {
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(LocalTime.MAX) : null;

        Page<Items> items = itemsRepository.searchItems(
                name, code, status, minQty, startDateTime, endDateTime, pageable
        );

        return items.map(mapper::toItemRes);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ApiResponse<ItemRes> create(CreateItemReq dto) {

        if (dto.getAssignItemOwner() == null || dto.getAssignItemOwner().isEmpty()) {
            throw new BadRequestException("assignItemOwner is required");
        }

        int totalQuantity = dto.getTotalQuantity();
        List<AssignItemOwnerReq> owners = dto.getAssignItemOwner();

        if (owners.size() == 1) {
            owners.get(0).setQuantity(totalQuantity);
        } else {
            int sum = owners.stream()
                    .map(AssignItemOwnerReq::getQuantity)
                    .mapToInt(q -> {
                        if (q == null || q <= 0) {
                            throw new BadRequestException("Quantity is required for multiple owners");
                        }
                        return q;
                    })
                    .sum();

            if (sum != totalQuantity) {
                throw new BadRequestException(
                        "Total quantity mismatch. Expected "
                                + totalQuantity + " but got " + sum
                );
            }
        }
        String generatedCode = itemCodeGenerator.generate();

        Items item = new Items();
        item.setCodeProduct(generatedCode);
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setTotalQuantity(totalQuantity);
        item.setStatus(dto.getStatus() != null ? dto.getStatus() : ItemStatus.AVAILABLE);

        Items savedItem = itemsRepository.save(item);
        for (AssignItemOwnerReq assign : owners) {
            ItemOwnerStocks stock = new ItemOwnerStocks();
            stock.setItem(savedItem);
            stock.setOwnerId(assign.getOwnerId());
            stock.setQuantity(assign.getQuantity());
            itemOwnerStocksRepository.save(stock);
        }
        return ApiResponse.success(
                "Create item successfully",
                mapper.toItemRes(savedItem)
        );
    }

    @Transactional
    public ApiResponse<ItemRes> update(UUID id, UpdateItemReq dto) {
        Items item = itemsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item with ID " + id + " not found"));

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setTotalQuantity(dto.getTotalQuantity());
        item.setStatus(dto.getStatus());
        item.setUpdatedAt(LocalDateTime.now());

        Items updated = itemsRepository.save(item);
        return ApiResponse.success("Update item successfully", mapper.toItemRes(updated));
    }

    public void updateItemStatus(UUID itemId) {
        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        Integer totalBorrowed = itemOwnerStocksRepository.getSumBorrowedQtyByItemId(itemId);
        if (totalBorrowed == null) totalBorrowed = 0;

        if (totalBorrowed >= item.getTotalQuantity()) {
            item.setStatus(ItemStatus.UNAVAILABLE);
        } else {
            item.setStatus(ItemStatus.AVAILABLE);
        }

        itemsRepository.save(item);
    }
}
