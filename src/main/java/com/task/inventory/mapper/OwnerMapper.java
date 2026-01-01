package com.task.inventory.mapper;

import com.task.inventory.dto.owner.OwnerRes;
import com.task.inventory.entity.Owners;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public OwnerRes toOwnerRes(Owners owners) {
        OwnerRes res = new OwnerRes();
        res.setId(owners.getId());
        res.setCodeOwner(owners.getCodeOwner());
        res.setName(owners.getName());
        res.setType(owners.getType());
        res.setActive(owners.getActive());
        res.setCreatedAt(owners.getCreatedAt());
        res.setUpdatedAt(owners.getUpdatedAt());
        return res;
    }
}
