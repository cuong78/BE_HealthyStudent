package com.healthy.backend.dto.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private String articleId;
    private String title;
    private String content;
    private String category;
    private String authorName;
    private int likes;
}