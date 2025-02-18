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
                .studentNotes(appointment.getStudentNote())
                .timeSlotID(appointment.getTimeSlotsID())
                .UpdatedAt(appointment.getUpdatedAt())
                .checkInTime(appointment.getCheckInTime())
                .checkOutTime(appointment.getCheckOutTime())
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
                .build();
    }
}
