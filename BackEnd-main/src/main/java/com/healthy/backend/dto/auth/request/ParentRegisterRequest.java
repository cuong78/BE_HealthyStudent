package com.healthy.backend.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.auth.ChildrenDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
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
public class ParentRegisterRequest extends RegisterRequest {

    @Schema(
            description = "A list of student IDs associated with the parent.",
            example = "[\"STU001\", \"STU002\", \"STU003\"]",
            implementation = ChildrenDetails.class
    )
    @Valid
    private ChildrenDetails childrenDetails;

    @Override
    public String getRole() {
        return "PARENT";
    }

    @Override
    public void setRole(String role) {
        super.setRole(Objects.requireNonNullElse(role, "PARENT"));
    }
}
