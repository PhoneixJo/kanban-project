package com.example.prello.board.controller;

import com.example.prello.board.dto.BoardRequestDto;
import com.example.prello.board.dto.BoardResponseDto;
import com.example.prello.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/boards")
public class BoardController {

    private final BoardService boardService;

    // 보드 생성
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(
            @PathVariable("workspaceId") Long workspaceId,
            @RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.createBoard(workspaceId, boardRequestDto);
        return new ResponseEntity<>(boardResponseDto, HttpStatus.CREATED);
    }

    // 보드 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(
            @PathVariable Long workspaceId,
            @PathVariable Long id,
            @RequestBody BoardRequestDto boardRequestDto
    ) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(workspaceId, id, boardRequestDto);
        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }

    // 보드 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(
            @PathVariable Long workspaceId,
            @PathVariable Long id
    ) {
        BoardResponseDto boardResponseDto = boardService.getBoard(workspaceId, id);
        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);
    }

    // 보드 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long workspaceId,
            @PathVariable Long id
    ) {
        String message = boardService.deleteBoard(workspaceId, id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
