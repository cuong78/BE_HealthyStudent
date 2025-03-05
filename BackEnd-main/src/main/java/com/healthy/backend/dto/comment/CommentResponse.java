package com.healthy.backend.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private Integer rating;
    private String authorName;
    private LocalDateTime createdAt;
    private List<CommentResponse> replies; // Reply dạng đệ quy (nested)
}
