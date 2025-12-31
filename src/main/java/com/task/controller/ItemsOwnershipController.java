package com.task.controller;

import com.task.dto.ApiResponse;
import com.task.dto.itemOwnerStock.AssignItemOwnerReq;
import com.task.dto.itemOwnerStock.ItemOwnerStockRes;
import com.task.dto.itemOwnerStock.UpdateItemOwnerStockReq;
import com.task.services.ItemOwnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/item-owner-stocks")
@RequiredArgsConstructor
public class ItemsOwnershipController {

    private final ItemOwnershipService itemOwnershipService;

    @GetMapping("/item/{itemId}")
    public ResponseEntity<ApiResponse<List<ItemOwnerStockRes>>> getByItemId(
            @PathVariable UUID itemId
    ) {
        return ResponseEntity.ok(itemOwnershipService.getByItemId(itemId));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<ApiResponse<List<ItemOwnerStockRes>>> getByOwnerId(
            @PathVariable UUID ownerId
    ) {
        return ResponseEntity.ok(itemOwnershipService.getByOwnerId(ownerId));
    }

    @GetMapping("/item/{itemId}/owner/{ownerId}")
    public ResponseEntity<ApiResponse<ItemOwnerStockRes>> getByItemAndOwner(
            @PathVariable UUID itemId,
            @PathVariable UUID ownerId
    ) {
        return ResponseEntity.ok(itemOwnershipService.getByItemAndOwner(itemId, ownerId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemOwnerStockRes>> assignItemsOwnership(
            @RequestBody AssignItemOwnerReq dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemOwnershipService.assign(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemOwnerStockRes>> updateItemsOwnership(
            @PathVariable UUID id,
            @RequestBody UpdateItemOwnerStockReq dto
    ) {
        return ResponseEntity.ok(itemOwnershipService.updateQuantity(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeItemsOwnership(
            @PathVariable UUID id
    ) {
        itemOwnershipService.remove(id);
        return ResponseEntity.noContent().build();
    }
}
