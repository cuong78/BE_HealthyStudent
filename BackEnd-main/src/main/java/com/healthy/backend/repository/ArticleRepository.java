package com.healthy.backend.repository;

import com.healthy.backend.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, String> {

    @Query("SELECT a.articleID FROM Article a ORDER BY a.articleID DESC LIMIT 1")
    String findLastArticleId();
    List<Article> findByCategory(String category);

    List<Article> findByAuthorId(String authorId);

    List<Article> findByTitleContainingOrContentContaining(String keyword, String keyword1);
} 