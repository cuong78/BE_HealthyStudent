package com.healthy.backend.mapper;

import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.ProgramParticipation;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Tags;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProgramMapper {

    public ProgramsResponse buildProgramsDetailsResponse(Programs program, List<StudentResponse> enrolled) {
        return ProgramsResponse.builder()
                .programID(program.getProgramID())
                .title(program.getProgramName())
                .description(program.getDescription())
                .numberParticipants(program.getNumberParticipants())
                .duration(program.getDuration())
                .status(program.getStatus())
                .startDate(LocalDate.from(program.getStartDate()))
                .facilitatorName(program.getPsychologists().getFullNameFromUser())
                .departmentName(program.getPsychologists().getDepartment().getName())
                .tags(program.getTags().stream().map(Tags::getTagName)
                        .collect(Collectors.toSet()))
                .type(program.getType())
                .meetingLink(program.getMeetingLink())
                .enrolled(enrolled)
                .build();
    }

    public ProgramsResponse buildProgramResponse(Programs program) {
        return ProgramsResponse.builder()
                .programID(program.getProgramID())
                .title(program.getProgramName())
                .description(program.getDescription())
                .numberParticipants(program.getNumberParticipants())
                .duration(program.getDuration())
                .status(program.getStatus())
                .startDate(LocalDate.from(program.getStartDate()))
                .facilitatorName(program.getPsychologists().getFullNameFromUser())
                .departmentName(program.getPsychologists().getDepartment().getName())
                .tags(program.getTags().stream().map(Tags::getTagName)
                        .collect(Collectors.toSet()))
                .type(program.getType())
                .meetingLink(program.getMeetingLink())
                .build();
    }

    public ProgramParticipationResponse buildProgramParticipationResponse(ProgramParticipation participation) {
        return ProgramParticipationResponse.builder()
                .programID(participation.getProgram().getProgramID())
                .programName(participation.getProgram().getProgramName())
                .description(participation.getProgram().getDescription())
                .numberParticipants(participation.getProgram().getNumberParticipants())
                .duration(participation.getProgram().getDuration())
                .status(participation.getStatus())
                .createdAt(participation.getProgram().getCreatedAt())
                .build();
    }
}
