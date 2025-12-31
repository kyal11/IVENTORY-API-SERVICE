package com.task.controller;

import com.task.dto.ApiResponse;
import com.task.dto.auth.LoginReq;
import com.task.dto.auth.LoginRes;
import com.task.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(LoginReq request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
