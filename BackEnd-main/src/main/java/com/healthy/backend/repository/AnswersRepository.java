package com.healthy.backend.repository;

import com.healthy.backend.entity.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswersRepository extends JpaRepository<Answers, String> {
} 