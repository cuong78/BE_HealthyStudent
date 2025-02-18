package com.healthy.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersRequest {
    @Schema(example = "US002")
    @NotBlank(message = "User ID is required")
    private String userId;
    @Schema(example = "John Doe")
    private String username;
    @Schema(example = "user@example.com")
    private String email;
}