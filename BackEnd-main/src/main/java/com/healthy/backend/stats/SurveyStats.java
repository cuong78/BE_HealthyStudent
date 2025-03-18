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
public class SurveyStats {
    private Map<String, Double> surveyParticipationRates; // key: survey category, value: percentage
    // getters & setters
}
