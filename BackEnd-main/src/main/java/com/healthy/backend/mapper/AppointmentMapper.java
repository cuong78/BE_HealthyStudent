package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.Appointments;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {


    public AppointmentResponse buildAppointmentResponse(
            Appointments appointment,
            PsychologistResponse psychologistResponse,
            StudentResponse studentResponse
    ) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .CreatedAt(appointment.getCreatedAt())
                .Status(appointment.getStatus().name())
                .psychologistResponse(psychologistResponse)
                .studentResponse(studentResponse)
                .psychologistNotes(appointment.getPsychologistNote())
                .studentNotes(appointment.getStudentNote())
                .timeSlotID(appointment.getTimeSlotsID())
                .startTime(appointment.getTimeSlot().getStartTime().toString())
                .endTime(appointment.getTimeSlot().getEndTime().toString())
                .UpdatedAt(appointment.getUpdatedAt())
                .checkInTime(appointment.getCheckInTime())
                .checkOutTime(appointment.getCheckOutTime())
                .build();
    }

    public AppointmentResponse buildBasicStudentAppointmentResponse(
            Appointments appointment,
            PsychologistResponse psychologistResponse) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .Status(appointment.getStatus().name())
                .startTime(String.valueOf(appointment.getTimeSlot().getStartTime()))
                .endTime(String.valueOf(appointment.getTimeSlot().getEndTime()))
                .psychologistID(psychologistResponse.getPsychologistId())
                .psychologistName(psychologistResponse.getName())
                .build();
    }

    public AppointmentResponse buildBasicPsychologistAppointmentResponse(
            Appointments appointment,
            StudentResponse studentResponse) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .Status(appointment.getStatus().name())
                .startTime(String.valueOf(appointment.getTimeSlot().getStartTime()))
                .endTime(String.valueOf(appointment.getTimeSlot().getEndTime()))
                .studentID(studentResponse.getStudentId())
                .studentName(studentResponse.getFullName())
                .build();
    }

    // For manager
    public AppointmentResponse buildBasicAppointmentResponse(
            Appointments appointment,
            StudentResponse studentResponse,
            PsychologistResponse psychologistResponse) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .Status(appointment.getStatus().name())
                .startTime(String.valueOf(appointment.getTimeSlot().getStartTime()))
                .endTime(String.valueOf(appointment.getTimeSlot().getEndTime()))
                .studentID(studentResponse.getStudentId())
                .studentName(studentResponse.getFullName())
                .psychologistID(psychologistResponse.getPsychologistId())
                .psychologistName(psychologistResponse.getName())
                .build();
    }

    public AppointmentResponse buildAppointmentResponse(
            Appointments appointment
    ) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .CreatedAt(appointment.getCreatedAt())
                .Status(appointment.getStatus().name())
                .studentNotes(appointment.getStudentNote())
                .timeSlotID(appointment.getTimeSlotsID())
                .UpdatedAt(appointment.getUpdatedAt())
                .checkInTime(appointment.getCheckInTime())
                .checkOutTime(appointment.getCheckOutTime())
                .psychologistNotes(appointment.getPsychologistNote())
                .build();
    }
}
