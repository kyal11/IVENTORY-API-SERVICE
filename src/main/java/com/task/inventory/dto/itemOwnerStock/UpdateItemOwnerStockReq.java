package com.task.inventory.dto.itemOwnerStock;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateItemOwnerStockReq {
    private UUID id;
    private Integer quantity;
}

