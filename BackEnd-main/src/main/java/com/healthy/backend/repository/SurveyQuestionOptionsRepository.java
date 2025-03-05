package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyQuestionOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionOptionsRepository extends JpaRepository<SurveyQuestionOptions, String> {
    SurveyQuestionOptions findByOptionID(String answerId);

    // @Query("SELECT an FROM Answers an ORDER BY an.answerID DESC")
    SurveyQuestionOptions findFirstByOrderByOptionIDDesc();

    List<SurveyQuestionOptions> findByQuestionID(String questionId);

    @Query("SELECT sqc.optionID FROM SurveyQuestionOptions sqc ORDER BY sqc.optionID DESC LIMIT 1")
    String findLastQuestionOptionId();
}