package com.healthy.backend.service;

import com.healthy.backend.dto.article.ArticleRequest;
import com.healthy.backend.dto.article.ArticleResponse;
import com.healthy.backend.entity.Article;
import com.healthy.backend.entity.Users;
import com.healthy.backend.mapper.ArticleMapper;
import com.healthy.backend.repository.ArticleRepository;
import com.healthy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final GeneralService __;
    private final ArticleMapper articleMapper;

    public ArticleResponse createArticle(ArticleRequest request) {
        Users author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Article article = Article.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .author(author)
                .likes(0)
                .build();

        articleRepository.save(article);
        return articleMapper.mapToResponse(article);
    }

    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll()
                .stream().map(articleMapper::mapToResponse)
                .toList();
    }

    public ArticleResponse getArticleById(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        return articleMapper.mapToResponse(article);
    }

    public ArticleResponse updateArticle(String articleId, ArticleRequest request) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setCategory(request.getCategory());

        articleRepository.save(article);
        return articleMapper.mapToResponse(article);
    }

    public void deleteArticle(String articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new RuntimeException("Article not found");
        }
        articleRepository.deleteById(articleId);
    }

    public List<ArticleResponse> getArticlesByCategory(String category) {
        return articleRepository.findByCategory(category)
                .stream().map(articleMapper::mapToResponse)
                .toList();
    }

    public List<ArticleResponse> getArticlesByAuthor(String authorId) {
        return articleRepository.findByAuthorId(authorId)
                .stream().map(articleMapper::mapToResponse)
                .toList();
    }

    public List<ArticleResponse> searchArticles(String keyword) {
        return articleRepository.findByTitleContainingOrContentContaining(keyword, keyword)
                .stream().map(articleMapper::mapToResponse)
                .toList();
    }

    public void likeArticle(String id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        article.setLikes(article.getLikes() + 1);
        articleRepository.save(article);
    }
}