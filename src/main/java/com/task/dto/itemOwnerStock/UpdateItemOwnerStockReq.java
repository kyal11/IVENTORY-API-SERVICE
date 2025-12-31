package com.task.dto.itemOwnerStock;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateItemOwnerStockReq {
    private UUID id;
    private Integer quantity;
}

