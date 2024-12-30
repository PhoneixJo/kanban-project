package com.example.prello.board.dto;

import com.example.prello.board.entity.Board;
import com.example.prello.deck.dto.DeckResponseWithCardsDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class BoardResponseDto {

    private final Long workspaceId;

    private final Long id;

    private final String title;

    private final String bgColor;

    private final String bgImage;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final List<DeckResponseWithCardsDto> decks;

    public static BoardResponseDto toDto(Board board, List<DeckResponseWithCardsDto> deckDtos) {
        return new BoardResponseDto(
                board.getWorkspace().getId(),
                board.getId(),
                board.getTitle(),
                board.getBg_color(),
                board.getBg_image(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                deckDtos
        );
    }
}
