package com.task.inventory.controller;

import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.auth.LoginReq;
import com.task.inventory.dto.auth.LoginRes;
import com.task.inventory.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(@RequestBody LoginReq request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
