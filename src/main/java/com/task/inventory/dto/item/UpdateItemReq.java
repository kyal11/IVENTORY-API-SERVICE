package com.task.inventory.dto.item;

import com.task.inventory.constant.ItemStatus;
import lombok.Data;

@Data
public class UpdateItemReq {

    private String name;
    private String description;
    private Integer totalQuantity;
    private ItemStatus status;
}
