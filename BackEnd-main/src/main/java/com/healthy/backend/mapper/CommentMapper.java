package com.healthy.backend.mapper;

import com.healthy.backend.dto.comment.CommentResponse;
import com.healthy.backend.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getRating(),
                comment.getAuthor().getFullName(),
                comment.getCreatedAt(),
                comment.getReplies().stream()
                        .map(this::mapToResponse)
                        .collect(Collectors.toList())
        );
    }
}