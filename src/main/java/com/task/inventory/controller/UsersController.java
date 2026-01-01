package com.task.inventory.controller;

import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.user.CreateUserReq;
import com.task.inventory.dto.user.UpdateUserReq;
import com.task.inventory.dto.user.UserRes;
import com.task.inventory.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserRes>>> getAll(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(usersService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRes>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(usersService.getById(id));
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserRes>> getByEmail(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(usersService.getByEmail(email));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserRes>> create(
            @RequestBody CreateUserReq request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usersService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserRes>> update(
            @PathVariable UUID id,
            @RequestBody UpdateUserReq request
    ) {
        return ResponseEntity.ok(usersService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> softDelete(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(usersService.softDelete(id));
    }
}
