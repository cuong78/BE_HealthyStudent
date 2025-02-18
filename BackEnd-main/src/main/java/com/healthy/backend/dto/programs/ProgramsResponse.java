package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.Tags;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramsResponse {
    @Schema(example = "US001")
    private String programID;
    @Schema(example = "Example Program")
    private String title;
    @Schema(example = "Program Description")
    private String description;
    @Schema(example = "2023-01-01")
    private LocalDate startDate;
    @Schema(example = "2")
    private Integer duration;
    @Schema(example = "10")
    private Integer numberParticipants;
    @Schema(example = "Active")
    private Programs.Status status;
    @Schema(example = "Prof. John Anderson")
    private String facilitatorName;
    @Schema(example = "Psychology")
    private String departmentName;
    @Schema(examples = {"tag1", "tag2", "tag3"})
    private Set<String> tags;
    @Schema(example = "2023-01-01")
    private LocalDate createdAt;
    @Schema(example = "Online")
    private Programs.Type type;
    @Schema(example = "https://zoom.us/j/123456789")
    private String meetingLink;
    @Schema(example = "10")
    private List<StudentResponse> enrolled;
}

//id: 14,
//title: "Mindful Leadership",
//description: "Develop leadership skills with a focus on mindfulness and emotional intelligence.",
//category: "Leadership",
//startDate: "2024-06-20",
//duration: "5 weeks",
//capacity: 20,
//enrolled: 13,
//status: "Open",
//facilitator: "Prof. John Anderson",
//tags: ["Leadership", "Mindfulness", "Personal Development"],