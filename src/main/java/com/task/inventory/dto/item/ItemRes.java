package com.task.inventory.dto.item;

import com.task.inventory.constant.ItemStatus;
import com.task.inventory.dto.itemOwnerStock.ItemOwnerStockRes;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ItemRes {

    private UUID id;
    private String codeProduct;
    private String name;
    private String description;
    private Integer totalQuantity;
    private ItemStatus status;

    private List<ItemOwnerStockRes> itemOwnerStockResList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
