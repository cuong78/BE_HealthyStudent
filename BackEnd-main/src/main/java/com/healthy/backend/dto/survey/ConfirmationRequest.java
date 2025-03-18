package com.healthy.backend.dto.survey;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmationRequest {
    private String studentID;
    private boolean confirmation;
}