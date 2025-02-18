package com.healthy.backend.mapper;

import com.healthy.backend.dto.psychologist.DepartmentResponse;
import com.healthy.backend.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentResponse buildDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .departmentId(department.getDepartmentID())
                .departmentName(department.getName())
                .build();
    }
}
