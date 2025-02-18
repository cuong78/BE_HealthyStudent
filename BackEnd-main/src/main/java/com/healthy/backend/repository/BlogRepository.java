package com.healthy.backend.repository;

import com.healthy.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Article, String> {
} 