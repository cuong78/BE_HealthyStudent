package com.healthy.backend.dto.notification;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    @Schema(example = "NOT001")
    private String id;
    @Schema(example = "Program Registration")
    private String title;
    @Schema(example = "You have a new program registration")
    private String message;
    @Schema(example = "Program")
    private String type;
    @Schema(example = "2023-01-01")
    private LocalDateTime createdAt;
    @Schema(example = "2023-01-01")
    private Boolean isRead;

    @Schema(example = "APP001")
    private String IDType; 
    
    private String UserID;
}