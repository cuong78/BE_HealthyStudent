package com.healthy.backend.repository;

import com.healthy.backend.entity.ProgramParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProgramParticipationRepository extends JpaRepository<ProgramParticipation, String> {

    List<ProgramParticipation> findByStudentID(String studentID);

    List<ProgramParticipation> findByProgramID(String programID);

    @Query("SELECT p.participationID FROM ProgramParticipation p ORDER BY p.participationID DESC LIMIT 1")
    String findLastParticipationCode();

    boolean existsByStudentID(String studentID);

    ProgramParticipation findByProgramIDAndStudentID(String programID, String studentId);

    @Query("SELECT p.studentID FROM ProgramParticipation p WHERE p.programID = :programID")
    List<String> findStudentIDsByProgramID(@Param("programID") String programID);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProgramParticipation p WHERE p.programID = :programId")
    void deleteByProgramId(@Param("programId") String programId);
}