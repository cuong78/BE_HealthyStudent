package com.healthy.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Article")
public class Article {

    @Id
    @Column(name = "ArticleID", length = 36)
    private String articleID;

    @Column(name = "Title", length = 100)
    private String title;

    @Column(name = "CreatedBy", length = 36)
    private String createdBy;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", insertable = false, updatable = false)
    private Users createdByUser;

    public Article(String articleID, String title, String createdBy, String content) {
        this.articleID = articleID;
        this.title = title;
        this.createdBy = createdBy;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 