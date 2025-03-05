package com.healthy.backend.controller;

import com.healthy.backend.dto.manager.AppointmentStatsResponse;
import com.healthy.backend.dto.manager.PsychologistStatsResponse;
import com.healthy.backend.enums.Role;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.ManagerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Manager Controller", description = "Manager specific APIs")
public class ManagerController {
    private final ManagerService managerService;
    private  final TokenService tokenService;
    // Endpoint to get appointment statistics by status
    @GetMapping("/stats/appointments")
    public AppointmentStatsResponse getAppointmentStats(
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.validateRole(httpRequest, Role.MANAGER) ) {
            throw new IllegalArgumentException("Unauthorized access get Appointments ");
        }

        return managerService.getAppointmentStats();
    }

    @GetMapping("/stats/psychologists")
    public List<PsychologistStatsResponse> getPsychologistStats(
            HttpServletRequest httpRequest
    ) {
        if (!tokenService.validateRole(httpRequest, Role.MANAGER) ) {
            throw new IllegalArgumentException("Unauthorized access get Appointments ");
        }
        return managerService.getPsychologistStats();
    }

}