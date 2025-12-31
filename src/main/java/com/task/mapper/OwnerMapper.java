package com.task.mapper;

import com.task.dto.owner.OwnerRes;
import com.task.entity.Owners;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
    public OwnerRes toOwnerRes(Owners owners) {
        OwnerRes res = new OwnerRes();
        res.setId(owners.getId());
        res.setName(owners.getName());
        res.setType(owners.getType());
        res.setActive(owners.getActive());
        res.setCreatedAt(owners.getCreatedAt());
        res.setUpdatedAt(owners.getUpdatedAt());
        return res;
    }
}
