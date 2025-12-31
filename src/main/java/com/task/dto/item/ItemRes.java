package com.task.dto.item;

import com.task.constant.ItemStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ItemRes {

    private UUID id;
    private String codeProduct;
    private String name;
    private String description;
    private Integer totalQuantity;
    private ItemStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
