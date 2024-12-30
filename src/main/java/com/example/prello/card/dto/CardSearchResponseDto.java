package com.example.prello.card.dto;

import com.example.prello.card.entity.Card;

import com.example.prello.comment.dto.CommentResponseDto;
import com.example.prello.comment.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class CardSearchResponseDto {

    private final Long cardId;
    private final String title;
    private final String description;
    private final LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String assigneeName;
    private final List<CommentResponseDto> comments;

    public CardSearchResponseDto(Card card, List<Comment> comments){
        this.cardId = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.endAt = card.getEndAt();
        this.createdAt = card.getCreatedAt();
        this.updatedAt = card.getUpdatedAt();
        this.assigneeName = card.getUser() != null ? card.getUser().getName() : null;
        this.comments = comments.stream().map(comment -> CommentResponseDto.toDto(comment)).collect(Collectors.toList());
    }

}
