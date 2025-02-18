package com.healthy.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationResponse {

    @Schema(example = "Success")
    private String message;

    @Schema(example = "eyJhbG.eyJzdW.TmCs6rN")
    private String token;

    @Schema(example = "true")
    private boolean verified;
}
