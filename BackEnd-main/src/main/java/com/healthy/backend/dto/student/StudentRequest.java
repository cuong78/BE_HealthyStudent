package com.healthy.backend.dto.student;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentRequest {
    @Schema(example = "STU001")
    private String studentID;
    @Schema(example = "Doe")
    private String name;
    @Schema(example = "stutdent@example.com")
    private String email;
    @Schema(example = "1234567890")
    private String phoneNumber;
    @Schema(example = "Example Address")
    private String address;
    @Schema(example = "10")
    private Integer grade;
    @Schema(example = "A")
    private String className;
    @Schema(example = "Example School")
    private String schoolName;
}
