package com.task.inventory.dto.owner;

import com.task.inventory.constant.OwnerType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateOwnerReq {

    @NotBlank
    private String name;

    @NotBlank
    private OwnerType type;

    @NotBlank
    private String codeOwner;

    private Boolean active;

}
