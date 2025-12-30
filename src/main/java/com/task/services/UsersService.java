package com.task.services;

import com.task.dto.ApiResponse;
import com.task.dto.user.UserRes;
import com.task.entity.Users;
import com.task.exception.NotFoundException;
import com.task.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRes toUserRes(Users user) {
        UserRes res = new UserRes();
        res.setId(user.getId());
        res.setUsername(user.getUsername());
        res.setEmail(user.getEmail());
        res.setActive(user.getActive());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());

        return res;
    }
    public ApiResponse<Page<UserRes>> getAll(Pageable pageable) {
        Page<UserRes> users = usersRepository.findAllByDeletedAtIsNull(pageable).map(this::toUserRes);
        return ApiResponse.success("Get all users successfully", users);
    }

    public ApiResponse<List<UserRes>> getAllWithoutPagination() {
        List<UserRes> users = usersRepository.findAllUsers().stream().map(this::toUserRes).collect(Collectors.toList());
        return ApiResponse.success("Get all users successfully", users);
    }

    public ApiResponse<UserRes> getById(UUID id) {
        Users user = usersRepository.findById(id)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        return ApiResponse.success("Get user by ID successfully", toUserRes(user));
    }

    public ApiResponse<UserRes> getByEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
        return ApiResponse.success("Get user by email successfully", toUserRes(user));
    }
}
