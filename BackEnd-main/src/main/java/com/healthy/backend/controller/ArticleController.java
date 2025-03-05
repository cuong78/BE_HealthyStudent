package com.healthy.backend.controller;

import com.healthy.backend.dto.article.ArticleRequest;
import com.healthy.backend.dto.article.ArticleResponse;
import com.healthy.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/articles")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Article Controller", description = "Article related APIs.")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    @Operation(
            deprecated = true,
            summary = "Create a new article",
            description = "Creates a new article."
    )
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody ArticleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.createArticle(request));
    }

    @GetMapping("/all")
    @Operation(
            deprecated = true,
            summary = "Get all articles",
            description = "Returns a list of all articles."
    )
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("")
    @Operation(
            deprecated = true,
            summary = "Get an article by ID",
            description = "Returns an article by ID."
    )
    public ResponseEntity<ArticleResponse> getArticleById(@RequestParam String articleId) {
        return ResponseEntity.ok(articleService.getArticleById(articleId));
    }

    @PutMapping("")
    @Operation(
            deprecated = true,
            summary = "Update an article",
            description = "Updates an existing article."
    )
    public ResponseEntity<ArticleResponse> updateArticle(@RequestParam String articleId, @RequestBody ArticleRequest request) {
        return ResponseEntity.ok(articleService.updateArticle(articleId, request));
    }

    @DeleteMapping("")
    @Operation(
            deprecated = true,
            summary = "Delete an article",
            description = "Deletes an existing article."
    )
    public ResponseEntity<Void> deleteArticle(@RequestParam String articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category")
    @Operation(
            deprecated = true,
            summary = "Get articles by category",
            description = "Returns a list of articles by category."
    )
    public ResponseEntity<List<ArticleResponse>> getArticlesByCategory(@RequestParam String category) {
        return ResponseEntity.ok(articleService.getArticlesByCategory(category));
    }

    @GetMapping("/author")
    @Operation(
            deprecated = true,
            summary = "Get articles by author ID",
            description = "Returns a list of articles by author ID."
    )
    public ResponseEntity<List<ArticleResponse>> getArticlesByAuthor(@RequestParam String authorId) {
        return ResponseEntity.ok(articleService.getArticlesByAuthor(authorId));
    }

    @GetMapping("/search")
    @Operation(
            deprecated = true,
            summary = "Search articles by keyword",
            description = "Returns a list of articles by keyword."
    )
    public ResponseEntity<List<ArticleResponse>> searchArticles(@RequestParam String keyword) {
        return ResponseEntity.ok(articleService.searchArticles(keyword));
    }

    @PostMapping("/like")
    @Operation(
            deprecated = true,
            summary = "Like an article",
            description = "Likes an article."
    )
    public ResponseEntity<Void> likeArticle(@RequestParam String articleId) {
        articleService.likeArticle(articleId);
        return ResponseEntity.ok().build();
    }
}