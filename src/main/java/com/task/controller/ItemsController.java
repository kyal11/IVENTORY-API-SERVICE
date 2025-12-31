package com.task.controller;

import com.task.constant.ItemStatus;
import com.task.dto.ApiResponse;
import com.task.dto.item.CreateItemReq;
import com.task.dto.item.ItemRes;
import com.task.dto.item.UpdateItemReq;
import com.task.services.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemRes>>> getAll() {
        return ResponseEntity.ok(itemsService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemRes>> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(itemsService.getById(id));
    }

    @GetMapping("/code/{codeProduct}")
    public ResponseEntity<ApiResponse<ItemRes>> getByCodeProduct(
            @PathVariable String codeProduct
    ) {
        return ResponseEntity.ok(itemsService.getByCodeProduct(codeProduct));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ItemRes>>> getByStatus(
            @PathVariable ItemStatus status
    ) {
        return ResponseEntity.ok(itemsService.getByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ItemRes>>> searchByName(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(itemsService.searchByName(name));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemRes>> create(
            @RequestBody CreateItemReq request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemsService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemRes>> update(
            @PathVariable UUID id,
            @RequestBody UpdateItemReq request
    ) {
        return ResponseEntity.ok(itemsService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(
            @PathVariable UUID id,
            @RequestParam ItemStatus status
    ) {
        return ResponseEntity.ok(itemsService.updateStatus(id, status));
    }
}
