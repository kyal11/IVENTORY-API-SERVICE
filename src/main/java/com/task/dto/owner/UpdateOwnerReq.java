package com.task.dto.owner;

import com.task.constant.OwnerType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UpdateOwnerReq {
    private String name;

    private OwnerType type;

    private String codeOwner;

    private Boolean active;
}
