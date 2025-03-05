package com.healthy.backend.mapper;

import com.healthy.backend.dto.auth.request.ParentRegisterRequest;
import com.healthy.backend.entity.Parents;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.Users;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParentMapper {

    public Parents buildParentEntity(ParentRegisterRequest parents, Users user,
                                     String parentID, List<Students> children) {
        return Parents.builder()
                .parentID(parentID)
                .userID(user.getUserId())
                .user(user)
                .students(children)
                .build();
    }
}
