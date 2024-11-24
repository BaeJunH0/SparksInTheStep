package com.sparksInTheStep.webBoard.comment.presentation.dto;

import com.sparksInTheStep.webBoard.auth.presentation.dto.MemberResponse;
import com.sparksInTheStep.webBoard.comment.application.dto.CommentInfo;
import com.sparksInTheStep.webBoard.post.controller.dto.PostResponse;

import java.time.LocalDateTime;

public record CommentResponse(
        String body,
        LocalDateTime createdAt,
        MemberResponse memberResponse,
        PostResponse postResponse
) {
    public static CommentResponse from(CommentInfo commentInfo){
        return new CommentResponse(
                commentInfo.body(),
                commentInfo.createdAt(),
                MemberResponse.from(commentInfo.memberInfo()),
                PostResponse.from(commentInfo.postInfo())
        );
    }
}
