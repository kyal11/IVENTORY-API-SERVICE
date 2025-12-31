package com.task.mapper;

import com.task.dto.user.UserRes;
import com.task.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserRes toUserRes(Users user) {
        UserRes res = new UserRes();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setEmail(user.getEmail());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());

        return res;
    }
}
