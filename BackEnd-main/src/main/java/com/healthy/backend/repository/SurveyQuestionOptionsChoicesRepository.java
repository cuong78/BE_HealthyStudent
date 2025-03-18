package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionOptionsChoicesRepository extends JpaRepository<SurveyQuestionOptionsChoices, String> {
    SurveyQuestionOptionsChoices findByQuestionIDAndOptionID(String questionId, String optionId);
    SurveyQuestionOptionsChoices findByOptionID(String optionId);
    List<SurveyQuestionOptionsChoices> findByQuestionID(String questionId);
    List<SurveyQuestionOptionsChoices> findByResultID(String resultId);


}