package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthy.backend.entity.Answers;
import com.healthy.backend.entity.SurveyResults;

public interface SurveyResultRepository extends JpaRepository<SurveyResults, String> {
    List<SurveyResults> findByStudentID(String userId);
    Answers findByQuestionIDAndAnswerID(String questionId, String answerId);
    Answers findByAnswerID(String answerId);

    @Query("SELECT sr FROM SurveyResults sr " +
            "JOIN FETCH sr.question q " +
            "JOIN FETCH q.category c " +
            "JOIN FETCH sr.answer a " +
            "WHERE sr.studentID = :studentId")
    List<SurveyResults> findByStudentIDWithDetails(@Param("studentId") String studentId);

}