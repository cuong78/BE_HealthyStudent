package com.healthy.backend.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStats {
    private Map<String, Double> statusDistribution; // key: status, value: percentage
    // getters & setters
}