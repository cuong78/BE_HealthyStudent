package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.Surveys;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Surveys, String> {

}