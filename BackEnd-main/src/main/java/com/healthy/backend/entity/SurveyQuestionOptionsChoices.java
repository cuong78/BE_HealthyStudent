package com.healthy.backend.entity;

import com.healthy.backend.enums.Identifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SurveyQuestionOptionsChoices")
public class SurveyQuestionOptionsChoices {

    @Id
    @Column(name = "OptionsChoices", length = 36, nullable = false)
    private String optionsChoicesID;

    @Column(name = "QuestionID", length = 36, nullable = false)
    private String questionID;

    @Column(name = "OptionID", length = 36, nullable = false)
    private String optionID;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionID", referencedColumnName = "QuestionID", insertable = false, updatable = false)
    private SurveyQuestions question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OptionID", referencedColumnName = "OptionID", insertable = false, updatable = false)
    private SurveyQuestionOptions options;

    @Column(name = "ResultID", length = 36, nullable = false)
    private String resultID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ResultID", referencedColumnName = "ResultID", insertable = false, updatable = false)
    private SurveyResult surveyResult;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public SurveyQuestionOptionsChoices(String resultID, String questionID, String optionsID) {
        this.optionsChoicesID = getOptionsChoicesID(resultID, questionID, optionsID);
        this.resultID = resultID;
        this.questionID = questionID;
        this.optionID = optionsID;
        this.createdAt = LocalDateTime.now();
    }

    private String getOptionsChoicesID(String resultID, String questionID, String optionsID) {
        String prefix = Identifier.SQC.toString();
        return String.format("%s%s%s%s", prefix,
                resultID.replaceFirst("^SRS", ""),
                questionID.replaceFirst("^SQR", ""),
                optionsID.replaceFirst("^SQO", ""));
    }
}