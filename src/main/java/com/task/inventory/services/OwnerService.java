package com.task.inventory.services;

import com.task.inventory.dto.ApiResponse;
import com.task.inventory.dto.owner.CreateOwnerReq;
import com.task.inventory.dto.owner.OwnerRes;
import com.task.inventory.dto.owner.UpdateOwnerReq;
import com.task.inventory.entity.Owners;
import com.task.inventory.exception.BadRequestException;
import com.task.inventory.exception.NotFoundException;
import com.task.inventory.mapper.OwnerMapper;
import com.task.inventory.repository.OwnersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnersRepository ownersRepository;
    private final OwnerMapper mapper;

    public ApiResponse<Page<OwnerRes>> getAllOwner(Pageable pageable) {
        Page<OwnerRes> owners = ownersRepository.findAll(pageable).map(mapper::toOwnerRes);
        return ApiResponse.success("Get all users successfully", owners);
    }

    public ApiResponse<List<OwnerRes>> getActiveOwner() {
        List<OwnerRes> owners = ownersRepository.findByActiveTrue()
                .stream()
                .map(mapper::toOwnerRes)
                .collect(Collectors.toList());

        return ApiResponse.success("Get active owners successfully", owners);
    }

    public ApiResponse<OwnerRes> getById(UUID id) {
        Owners owner = ownersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Owner with ID " + id + " not found"));

        return ApiResponse.success("Get owner successfully", mapper.toOwnerRes(owner));
    }

    public ApiResponse<OwnerRes> getByCodeOwner(String codeOwner) {
        Owners owner = ownersRepository.findByCodeOwner(codeOwner)
                .orElseThrow(() -> new NotFoundException("Owner with code " + codeOwner + " not found"));

        return ApiResponse.success("Get owner successfully", mapper.toOwnerRes(owner));
    }

    @Transactional
    public ApiResponse<OwnerRes> create(CreateOwnerReq dto) {
        if (ownersRepository.existsByCodeOwner(dto.getCodeOwner()) ){
            throw new BadRequestException("Owner code already exists");
        }

        Owners owner = new Owners();
        owner.setCodeOwner(dto.getCodeOwner());
        owner.setName(dto.getName());
        owner.setType(dto.getType());
        owner.setActive(true);
        owner.setCreatedAt(LocalDateTime.now());
        owner.setUpdatedAt(LocalDateTime.now());

        Owners saved = ownersRepository.save(owner);
        return ApiResponse.success("Create owner successfully", mapper.toOwnerRes(saved));
    }

    @Transactional
    public ApiResponse<OwnerRes> update(UUID id, UpdateOwnerReq dto) {
        Owners owner = ownersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Owner with ID " + id + " not found"));

        owner.setName(dto.getName());
        owner.setType(dto.getType());
        owner.setUpdatedAt(LocalDateTime.now());

        Owners updated = ownersRepository.save(owner);
        return ApiResponse.success("Update owner successfully", mapper.toOwnerRes(updated));
    }

    @Transactional
    public ApiResponse<String> activate(UUID id) {
        Owners owner = ownersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Owner with ID " + id + " not found"));

        owner.setActive(true);
        owner.setUpdatedAt(LocalDateTime.now());
        ownersRepository.save(owner);

        return ApiResponse.success("Owner activated successfully");
    }

    @Transactional
    public ApiResponse<String> deactivate(UUID id) {
        Owners owner = ownersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Owner with ID " + id + " not found"));

        owner.setActive(false);
        owner.setUpdatedAt(LocalDateTime.now());
        ownersRepository.save(owner);

        return ApiResponse.success("Owner deactivated successfully");
    }
}
