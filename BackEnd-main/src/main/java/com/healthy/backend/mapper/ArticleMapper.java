package com.healthy.backend.mapper;

import com.healthy.backend.dto.article.ArticleResponse;
import com.healthy.backend.entity.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {
    public ArticleResponse mapToResponse(Article article) {
        return new ArticleResponse(
                article.getArticleID(),
                article.getTitle(),
                article.getContent(),
                article.getCategory(),
                article.getAuthor().getFullName(),
                article.getLikes()
        );
    }
}