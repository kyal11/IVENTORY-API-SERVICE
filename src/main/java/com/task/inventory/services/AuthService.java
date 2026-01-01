package com.task.inventory.services;

import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.auth.LoginReq;
import com.task.inventory.dto.auth.LoginRes;
import com.task.inventory.entity.Users;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public ApiResponse<LoginRes> login(LoginReq dto) {
        Users user = usersRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Email not Found"));

        if (user.getDeletedAt() != null || !bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Credential");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());

        LoginRes response = new LoginRes(user.getName(), user.getEmail(), token);

        return  ApiResponse.success("Successfully login!", response);
    }
}
