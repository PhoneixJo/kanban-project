package com.example.prello.deck.dto;

import com.example.prello.card.dto.CardResponseDto;
import com.example.prello.deck.entity.Deck;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DeckResponseWithCardsDto {

    private final Long id;
    private final String title;
    private final int order;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<CardResponseDto> cards; // 카드 리스트 포함

    public static DeckResponseWithCardsDto toDto(Deck deck, List<CardResponseDto> cards) {
        return new DeckResponseWithCardsDto(
                deck.getId(),
                deck.getTitle(),
                deck.getOrder(),
                deck.getCreatedAt(),
                deck.getUpdatedAt(),
                cards
        );
    }
}