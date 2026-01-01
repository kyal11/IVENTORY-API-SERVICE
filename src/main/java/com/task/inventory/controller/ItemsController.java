package com.task.inventory.controller;

import com.task.inventory.constant.ItemStatus;
import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.item.CreateItemReq;
import com.task.inventory.dto.item.ItemRes;
import com.task.inventory.dto.item.TrackItemRes;
import com.task.inventory.dto.item.UpdateItemReq;
import com.task.inventory.services.ItemsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemsController {

    private final ItemsService itemsService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ItemRes>>> getAll(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(itemsService.getAll(pageable));
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

    @GetMapping("/tracking/{codeProduct}")
    public ResponseEntity<ApiResponse<TrackItemRes>> getTrackingItem(
            @PathVariable String codeProduct
    ) {
        return ResponseEntity.ok(itemsService.getItemTrack(codeProduct));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ItemRes>>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) ItemStatus status,
            @RequestParam(required = false) Integer minQty,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable
    ) {
        Page<ItemRes> result = itemsService.search(name, code, status, minQty, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success("Items fetched successfully", result));
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
}
