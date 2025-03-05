package com.healthy.backend.dto.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Content can not blank")
    private String content;

    // ID của bình luận cha nếu muốn reply vào bình luận đã có (nếu null thì là bình luận gốc)
    private Long parentCommentId;

    @Min(1) @Max(5) Integer rating;
}
