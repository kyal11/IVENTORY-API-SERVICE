package com.task.inventory.dto.item;

import com.task.inventory.constant.ItemStatus;
import com.task.inventory.dto.itemOwnerStock.AssignItemOwnerReq;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateItemReq {
    @NotBlank
    private String name;
    private String description;

    @NotBlank
    private Integer totalQuantity;

    private ItemStatus status;

    private List<AssignItemOwnerReq> assignItemOwner;
}
