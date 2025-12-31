package com.task.controller;

import com.task.dto.ApiResponse;
import com.task.dto.owner.CreateOwnerReq;
import com.task.dto.owner.OwnerRes;
import com.task.dto.owner.UpdateOwnerReq;
import com.task.services.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;


    @GetMapping
    public ResponseEntity<ApiResponse<List<OwnerRes>>> getAllOwner() {
        return ResponseEntity.ok(ownerService.getAllOwner());
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<OwnerRes>>> getActiveOwner() {
        return ResponseEntity.ok(ownerService.getActiveOwner());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OwnerRes>> getById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(ownerService.getById(id));
    }

    @GetMapping("/code/{codeOwner}")
    public ResponseEntity<ApiResponse<OwnerRes>> getByCodeOwner(
            @PathVariable String codeOwner
    ) {
        return ResponseEntity.ok(ownerService.getByCodeOwner(codeOwner));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<OwnerRes>> create(
            @RequestBody CreateOwnerReq request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ownerService.create(request));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OwnerRes>> update(
            @PathVariable UUID id,
            @RequestBody UpdateOwnerReq request
    ) {
        return ResponseEntity.ok(ownerService.update(id, request));
    }


    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<String>> activate(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(ownerService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<String>> deactivate(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(ownerService.deactivate(id));
    }
}

