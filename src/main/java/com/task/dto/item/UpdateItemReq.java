package com.task.dto.item;

import com.task.constant.ItemStatus;
import lombok.Data;

@Data
public class UpdateItemReq {

    private String name;
    private String description;
    private Integer totalQuantity;
    private ItemStatus status;
}
