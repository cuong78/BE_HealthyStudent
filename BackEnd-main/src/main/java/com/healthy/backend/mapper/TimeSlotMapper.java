package com.healthy.backend.mapper;

import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.TimeSlots;
import org.springframework.stereotype.Component;

@Component
public class TimeSlotMapper {
    public TimeSlotResponse toResponse(TimeSlots timeSlot) {
        return new TimeSlotResponse(
                timeSlot.getTimeSlotsID(),
                timeSlot.getSlotDate(),
                timeSlot.getStartTime(),
                timeSlot.getEndTime(),
                timeSlot.getStatus(),
                timeSlot.getMaxCapacity(),
                timeSlot.getCurrentBookings(),
                timeSlot.getBooked()
        );
    }

}