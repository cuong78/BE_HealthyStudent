package com.healthy.backend.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.auth.PsychologistDetails;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class PsychologistRegisterRequest extends RegisterRequest {
    @Valid
    @Schema(name = "psychologistDetails", implementation = PsychologistDetails.class)
    @NotNull(message = "Psychologist details are required")
    private PsychologistDetails psychologistDetails;

    @Override
    public String getRole() {
        return "PSYCHOLOGIST";
    }

    @Override
    public void setRole(String role) {
        super.setRole(Objects.requireNonNullElse(role, "PSYCHOLOGIST"));
    }
}
