package com.example.prello.card.dto;

import com.example.prello.card.entity.Card;
import com.example.prello.comment.dto.CommentResponseDto;
import com.example.prello.comment.entity.Comment;
import lombok.Getter;

import java.util.List;


@Getter
public class CardDetailResponseDto extends CardResponseDto {

    public CardDetailResponseDto(Card card, List<Comment> comments) {
        super(card.getId(), card.getTitle(), card.getDescription(), card.getEndAt(), card.getCreatedAt(), card.getUpdatedAt());
        this.comments = comments.stream().map(CommentResponseDto::toDto).toList();
    }

    private final List<CommentResponseDto> comments;
}
