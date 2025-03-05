package com.healthy.backend.dto.psychologist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsychologistResponse {

    @Schema(example = "PSY001")
    @NotBlank(message = "Psychologist ID is required")
    private String psychologistId;
    @Schema(example = "Psychologist Name")
    private String name;
    @Schema(example = "Department Name")
    private String departmentName;
    @Schema(example = "10")
    private Integer yearsOfExperience;
    @Schema(example = "Active")
    private String status;
    @Schema(example = "")
    private UsersResponse info;
    @Schema(example = "")
    private List<AppointmentResponse> appointment;
}
