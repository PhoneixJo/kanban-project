package com.example.prello.card.dto;

import com.example.prello.card.entity.Card;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CardResponseDto {

    private final Long id;

    private final String title;

    private final String description;

    private final LocalDateTime endAt;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static CardResponseDto toDto(Card card) {
        return new CardResponseDto(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getEndAt(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}
