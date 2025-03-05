package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestions, String> {
    List<SurveyQuestions> findByQuestionIDIn(List<String> QuestionId) ;
    SurveyQuestions findByQuestionIDAndSurveyID(String QuestionID, String surveyID);
    List<SurveyQuestions> findBySurveyID(String surveyID);
    List<SurveyQuestions> findByQuestionID (String questionId);

    // @Query("SELECT sq from SurveyQuestions sq ORDER BY sq.questionID DESC")
    SurveyQuestions findFirstByOrderByQuestionIDDesc();

    @Query("SELECT COUNT(sq) FROM SurveyQuestions sq WHERE sq.surveyID = :surveyId ")
    int countQuestionInSuv(@Param("surveyId") String surveyId);

    long countBySurveyID(String surveyID);

    @Query("SELECT q.questionID FROM SurveyQuestions q ORDER BY q.questionID DESC LIMIT 1")
    String findLastQuestionId();
} 