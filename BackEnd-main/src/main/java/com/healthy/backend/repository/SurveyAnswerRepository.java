package com.healthy.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.Answers;





public interface SurveyAnswerRepository extends JpaRepository<Answers, String> {
    

}