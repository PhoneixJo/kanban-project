package com.example.prello.card.controller;

import com.example.prello.card.dto.CardSearchResponseDto;
import com.example.prello.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}")
public class SearchController {

    private final CardService cardService;

    @GetMapping("/search")
    public ResponseEntity<Page<CardSearchResponseDto>> findBySearch(
            @PathVariable Long workspaceId,
            @RequestParam(required = false) Long boardId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDateTime endAt,
            @RequestParam(required = false) String assigneeName,
            @PageableDefault(size = 10, page = 0, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
            ) {

        Page<CardSearchResponseDto> cards = cardService.findBySearch(workspaceId, boardId, title, description, endAt, assigneeName, pageable);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

}
