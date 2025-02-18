package com.healthy.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Answers")
public class Answers {

    @Id
    @Column(name = "AnswerID", length = 36, nullable = false)
    private String answerID;

    @Column(name = "QuestionID", length = 36, nullable = false)
    private String questionID;

    @Column(name = "Answer", columnDefinition = "TEXT", nullable = false)
    private String answer;

    @Column(name = "Score")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionID", referencedColumnName = "QuestionID", insertable = false, updatable = false)
    private SurveyQuestions question;

    public Answers(String answerID, String questionID, String answer, Integer score) {
        this.answerID = answerID;
        this.questionID = questionID;
        this.answer = answer;
        this.score = score;
    }
} 