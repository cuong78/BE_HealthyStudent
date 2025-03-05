package com.healthy.backend.repository;

import com.healthy.backend.entity.DefaultTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultTimeSlotRepository extends JpaRepository<DefaultTimeSlot, String> {
}
