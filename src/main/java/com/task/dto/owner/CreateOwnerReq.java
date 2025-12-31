package com.task.dto.owner;

import com.task.constant.OwnerType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

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
