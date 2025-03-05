package com.healthy.backend.repository;

import com.healthy.backend.entity.Surveys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends JpaRepository<Surveys, String> {

    @Query("SELECT s.surveyID FROM Surveys s ORDER BY s.surveyID DESC LIMIT 1")
    String findLastSurveyId();

}