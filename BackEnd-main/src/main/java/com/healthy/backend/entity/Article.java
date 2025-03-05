package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Article")
public class Article {

    @Id
    @Column(name = "ArticleID", length = 36)
    private String articleID;

    @Column(name = "Title", length = 100)
    private String title;

    @Column(name = "AuthorID", length = 36)
    private String authorId;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private int likes = 0;

    @JoinColumn(name = "Author", insertable = false, updatable = false)
    private Users author;

    @Column(name = "Category", length = 100)
    private String category;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "CreatedBy", insertable = false, updatable = false)
    private Users createdByUser;

    public Article(String articleID, String title, String authorId, String content) {
        this.articleID = articleID;
        this.title = title;
        this.authorId = authorId;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 