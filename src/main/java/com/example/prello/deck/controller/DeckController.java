package com.example.prello.deck.controller;

import com.example.prello.deck.dto.DeckRequestDto;
import com.example.prello.deck.dto.DeckResponseDto;
import com.example.prello.deck.service.DeckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/boards/{boardId}/decks")
public class DeckController {

    private final DeckService deckService;

    //리스트 생성
    @PostMapping
    public ResponseEntity<DeckResponseDto> createDeck(
        @PathVariable Long workspaceId,
        @PathVariable Long boardId,
        @Valid @RequestBody DeckRequestDto dto) {

        DeckResponseDto deckResponseDto = deckService.createDeck(workspaceId, boardId, dto);
        return new ResponseEntity<>(deckResponseDto, HttpStatus.CREATED);
    }

    //리스트 제목 수정
    @PutMapping("/{listId}")
    public ResponseEntity<DeckResponseDto> updateDeckTitle(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long id,
            @Valid @RequestBody DeckRequestDto dto) {

             DeckResponseDto deckResponseDto = deckService.updateDeckTitle(workspaceId, boardId, id, dto);
             return new ResponseEntity<>(deckResponseDto, HttpStatus.OK);
    }

    //리스트 순서 수정
    @PutMapping("/{listId}/order")
    public ResponseEntity<DeckResponseDto> updateDeckOrder(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long id,
            @Valid @RequestBody DeckRequestDto dto) {

            DeckResponseDto deckResponseDto = deckService.updateDeckTitle(workspaceId, boardId, id, dto);
            return new ResponseEntity<>(deckResponseDto, HttpStatus.OK);
    }
    //리스트 삭제
    @DeleteMapping("/{listId}")
    public ResponseEntity<DeckResponseDto> deleteDeck(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long id) {

            deckService.deleteDeck(workspaceId, boardId, id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
