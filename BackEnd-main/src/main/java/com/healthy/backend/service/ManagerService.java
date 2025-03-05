package com.healthy.backend.service;

import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final AppointmentRepository appointmentRepository;
    private final PsychologistRepository psychologistRepository;

    // Method to get appointment statistics
    public AppointmentStatsResponse getAppointmentStats() {
        List<Appointments> appointments = appointmentRepository.findAll();
        Map<String, Long> appointmentCounts = appointments.stream()
                .collect(Collectors.groupingBy(appointment -> appointment.getStatus().name(), Collectors.counting()));

        AppointmentStatsResponse response = new AppointmentStatsResponse();
        response.setAppointmentCounts(appointmentCounts);
        return response;
    }

    public List<PsychologistStatsResponse> getPsychologistStats() {
        List<Psychologists> psychologists = psychologistRepository.findAll();
        List<Appointments> allAppointments = appointmentRepository.findAll();

        List<PsychologistStatsResponse> responses = new ArrayList<>();
        for (Psychologists psychologist : psychologists) {
            // Filter appointments belonging to the current psychologist
            List<Appointments> filteredAppointments = allAppointments.stream()
                    .filter(a -> psychologist.getPsychologistID().equals(a.getPsychologistID()))
                    .collect(Collectors.toList());

            long appointmentCount = filteredAppointments.size();
            double averageRating = filteredAppointments.stream()
                    .filter(a -> a.getRating() != null)
                    .mapToInt(Appointments::getRating)
                    .average()
                    .orElse(0.0);

            PsychologistStatsResponse statResponse = new PsychologistStatsResponse();
            statResponse.setPsychologistId(psychologist.getPsychologistID());
            statResponse.setFullName(psychologist.getFullNameFromUser());
            statResponse.setAppointmentCount(appointmentCount);
            statResponse.setAverageRating(averageRating);
            responses.add(statResponse);
        }
        return responses;
    }



}