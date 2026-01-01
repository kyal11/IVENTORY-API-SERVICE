package com.task.inventory.services;

import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.user.CreateUserReq;
import com.task.inventory.dto.user.UpdateUserReq;
import com.task.inventory.dto.user.UserRes;
import com.task.inventory.entity.Users;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.UserMapper;
import com.task.inventory.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper mapper;
    public ApiResponse<Page<UserRes>> getAll(Pageable pageable) {
        Page<UserRes> users = usersRepository.findAllByDeletedAtIsNull(pageable).map(mapper::toUserRes);
        return ApiResponse.success("Get all users successfully", users);
    }

    public ApiResponse<List<UserRes>> getAllWithoutPagination() {
        List<UserRes> users = usersRepository.findAllUsers().stream().map(mapper::toUserRes).collect(Collectors.toList());
        return ApiResponse.success("Get all users successfully", users);
    }

    public ApiResponse<UserRes> getById(UUID id) {
        Users user = usersRepository.findById(id)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        return ApiResponse.success("Get user by ID successfully", mapper.toUserRes(user));
    }

    public ApiResponse<UserRes> getByName(String username) {
        Users user = usersRepository.findByName(username)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User " + username + " not found"));
        return ApiResponse.success("Get user by username successfully", mapper.toUserRes(user));
    }

    public ApiResponse<UserRes> getByEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with email " + email + " not found"));
        return ApiResponse.success("Get user by email successfully", mapper.toUserRes(user));
    }

    @Transactional
    public ApiResponse<UserRes> create(CreateUserReq dto) {
        if (usersRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        Users user = new Users();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getUserRole());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Users savedUser = usersRepository.save(user);
        return ApiResponse.success("Create user successfully", mapper.toUserRes(user));
    }

    @Transactional
    public ApiResponse<UserRes> update(UUID id, UpdateUserReq dto) {
        Users userToUpdate = usersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        if (dto.getPassword() != null) {
            userToUpdate.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }
        userToUpdate.setName(dto.getName());
        userToUpdate.setEmail(dto.getEmail());
        userToUpdate.setRole(dto.getUserRole());
        userToUpdate.setUpdatedAt(LocalDateTime.now());


        Users updatedUser = usersRepository.save(userToUpdate);
        return ApiResponse.success("Update user successfully", mapper.toUserRes(updatedUser));
    }

    @Transactional
    public ApiResponse<String> softDelete(UUID id) {
        Users user = usersRepository.findById(id)
                .filter(u -> u.getDeletedAt() == null)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
        usersRepository.softDeleteById(id);

        return ApiResponse.success("Delete user successfully!");
    }
}
