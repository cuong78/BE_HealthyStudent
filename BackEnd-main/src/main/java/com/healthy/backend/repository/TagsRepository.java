package com.healthy.backend.repository;

import com.healthy.backend.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagsRepository extends JpaRepository<Tags, String> {

    @Query("SELECT t.tagId FROM Tags t ORDER BY t.tagId DESC LIMIT 1")
    String findLastTagId();

    Optional<Tags> findByTagName(String tagName);
}
