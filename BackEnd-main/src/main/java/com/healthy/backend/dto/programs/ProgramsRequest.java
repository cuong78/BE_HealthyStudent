package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramsRequest {
    @Schema(example = "US005")
    private String userId;
    @Schema(example = "Program Name")
    private String name;
    @Schema(example = "Program Description")
    private String description;
    @Schema(example = "10")
    private Integer numberParticipants;
    @Schema(example = "2")
    private Integer duration;
    @Schema(example = "2023-01-01")
    private String startDate;
    @Schema(example = "Active")
    private String status;
    @Schema(examples = {"tag1", "tag2", "tag3"})
    private HashSet<String> tags;
    @Schema(example = "")
    private String facilitatorId;
    @Schema(example = "")
    private String departmentId;
    @Schema(example = "Online")
    private String type;
    @Schema(example = "https://zoom.us/j/123456789")
    private String meetingLink;
}
