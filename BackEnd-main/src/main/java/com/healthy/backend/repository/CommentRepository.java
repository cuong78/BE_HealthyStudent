package com.healthy.backend.repository;

import com.healthy.backend.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAppointment_AppointmentIDAndParentCommentIsNullOrderByCreatedAtAsc(String appointmentID);

    List<Comment> findByPrograms_ProgramIDAndParentCommentIsNullOrderByCreatedAtAsc(String programID);

    List<Comment> findByArticle_ArticleIDAndParentCommentIsNullOrderByCreatedAtAsc(String articleID);

    boolean existsByProgramsAndAuthorAndRatingIsNotNull(
            @Param("appointment") Programs programs,
            @Param("author") Users author
    );

    boolean existsByArticleAndAuthorAndRatingIsNotNull(
            @Param("appointment") Article article,
            @Param("author") Users author
    );

    boolean existsByAppointmentAndAuthorAndRatingIsNotNull(
            @Param("appointment") Appointments appointment,
            @Param("author") Users author
    );

}
