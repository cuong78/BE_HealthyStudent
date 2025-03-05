package com.healthy.backend.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.auth.StudentDetails;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentRegisterRequest extends RegisterRequest {
    @Valid
    @NotNull(message = "Student details are required")
    private StudentDetails studentDetails;

    @Override
    public String getRole() {
        return "STUDENT";
    }

    @Override
    public void setRole(String role) {
        super.setRole(Objects.requireNonNullElse(role, "STUDENT"));
    }
}