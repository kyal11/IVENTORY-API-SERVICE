package com.task.inventory.dto.owner;

import com.task.inventory.constant.OwnerType;
import lombok.Data;

@Data
public class UpdateOwnerReq {
    private String name;

    private OwnerType type;

    private String codeOwner;

    private Boolean active;
}
