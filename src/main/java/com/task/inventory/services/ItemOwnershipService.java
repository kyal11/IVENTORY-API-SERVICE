package com.task.inventory.services;

import com.task.inventory.constant.ItemLogType;
import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.itemOwnerStock.AssignItemOwnerReq;
import com.task.inventory.dto.itemOwnerStock.ItemOwnerStockRes;
import com.task.inventory.dto.itemOwnerStock.UpdateItemOwnerStockReq;
import com.task.inventory.entity.ItemOwnerStocks;
import com.task.inventory.entity.Items;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.ItemMapper;
import com.task.inventory.repository.ItemOwnerStocksRepository;
import com.task.inventory.repository.ItemsRepository;
import com.task.inventory.security.SecurityUtils;
import com.task.inventory.utils.ObjectToJson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemOwnershipService {
    private final ItemOwnerStocksRepository itemOwnerStocksRepository;
    private final ItemsRepository itemsRepository;
    private final ItemMapper mapper;

    private final ObjectToJson objectToJson;
    private final ItemLogsService itemLogsService;

    public ApiResponse<List<ItemOwnerStockRes>> getByItemId(UUID itemId) {
        List<ItemOwnerStockRes> stocks = itemOwnerStocksRepository.findByItemId(itemId)
                .stream()
                .map(mapper::toItemOwnerStockRes)
                .collect(Collectors.toList());

        return ApiResponse.success("Get item owner stocks successfully", stocks);
    }

    public ApiResponse<List<ItemOwnerStockRes>> getByOwnerId(UUID ownerId) {
        List<ItemOwnerStockRes> stocks = itemOwnerStocksRepository.findByOwnerId(ownerId)
                .stream()
                .map(mapper::toItemOwnerStockRes)
                .collect(Collectors.toList());

        return ApiResponse.success("Get owner stocks successfully", stocks);
    }

    public ApiResponse<ItemOwnerStockRes> getByItemAndOwner(UUID itemId, UUID ownerId) {
        ItemOwnerStocks stock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(itemId, ownerId)
                .orElseThrow(() ->
                        new NotFoundException("Stock not found for item & owner"));

        return ApiResponse.success("Get stock successfully", mapper.toItemOwnerStockRes(stock));
    }

    @Transactional
    public ApiResponse<ItemOwnerStockRes> assign(AssignItemOwnerReq dto) {

        Items item = itemsRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (itemOwnerStocksRepository.existsByItemIdAndOwnerId(
                dto.getItemId(), dto.getOwnerId())) {
            throw new BadRequestException("Item already assigned to this owner");
        }

        if (dto.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        ItemOwnerStocks stock = new ItemOwnerStocks();
        stock.setItem(item);
        stock.setOwnerId(dto.getOwnerId());
        stock.setQuantity(dto.getQuantity());

        ItemOwnerStocks saved = itemOwnerStocksRepository.save(stock);


        itemLogsService.log(
                item.getId(),
                null,
                ItemLogType.ASSIGN_OWNER,
                "Assign item to owner",
                objectToJson.toJson(mapper.toItemOwnerStockRes(stock)),
                objectToJson.toJson(mapper.toItemOwnerStockRes(saved)),
                SecurityUtils.getCurrentUserId()
        );

        return ApiResponse.success("Assign item to owner successfully", mapper.toItemOwnerStockRes(saved));
    }

    @Transactional
    public ApiResponse<ItemOwnerStockRes> updateQuantity(
            UUID itemId,
            UpdateItemOwnerStockReq dto
    ) {
        ItemOwnerStocks stock = itemOwnerStocksRepository
                .findByItemIdAndOwnerId(itemId, dto.getId())
                .orElseThrow(() ->
                        new NotFoundException("Stock not found for item & owner"));

        String beforeState = objectToJson.toJson(
                mapper.toItemOwnerStockRes(stock)
        );

        if (dto.getQuantity() < 0) {
            throw new BadRequestException("Quantity cannot be negative");
        }

        stock.setQuantity(dto.getQuantity());
        ItemOwnerStocks updated = itemOwnerStocksRepository.save(stock);

        itemLogsService.log(
                itemId,
                null,
                ItemLogType.ASSIGN_OWNER,
                "Update item owner stock quantity",
                beforeState,
                objectToJson.toJson(mapper.toItemOwnerStockRes(updated)),
                SecurityUtils.getCurrentUserId()
        );
        return ApiResponse.success("Update stock successfully", mapper.toItemOwnerStockRes(updated));
    }
}
