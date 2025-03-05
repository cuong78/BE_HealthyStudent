package com.healthy.backend.dto.auth.response;

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
public class VerificationResponse {

    @Schema(example = "Success")
    private String message;

    @Schema(example = "eyJhbG.eyJzdW.TmCs6rN")
    private String token;

    @Schema(example = "true")
    private boolean verified;
}
