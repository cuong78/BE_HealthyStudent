package com.healthy.backend.mapper;

import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.TimeSlots;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeSlotMapper {
    public TimeSlotResponse buildResponse(TimeSlots timeSlot) {
        return TimeSlotResponse.builder()
                .timeSlotId(timeSlot.getTimeSlotsID())
                .slotDate(timeSlot.getSlotDate())
                .startTime(timeSlot.getStartTime())
                .endTime(timeSlot.getEndTime())
                .status(timeSlot.getStatus().name())
                .build();
    }
    public List<TimeSlotResponse> buildResponse(List<TimeSlots> timeSlots) {
        return timeSlots.stream().map(this::buildResponse).toList();
    }
}