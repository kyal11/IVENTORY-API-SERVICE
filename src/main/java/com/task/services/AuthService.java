package com.task.services;

import com.task.dto.ApiResponse;
import com.task.dto.auth.LoginReq;
import com.task.dto.auth.LoginRes;
import com.task.entity.Users;
import com.task.exception.BadRequestException;
import com.task.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private UsersRepository usersRepository;
    private JwtService jwtService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public ApiResponse<LoginRes> login(LoginReq dto) {
        Users user = usersRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadRequestException("Email not Found"));

        if (user.getDeletedAt() != null || !bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Credential");
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());

        LoginRes response = new LoginRes(user.getName(), user.getEmail(), token);

        return  ApiResponse.success("Succesfully login!", response);
    }
}
