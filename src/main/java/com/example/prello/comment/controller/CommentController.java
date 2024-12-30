package com.example.prello.comment.controller;

import com.example.prello.comment.dto.CommentRequestDto;
import com.example.prello.comment.dto.CommentResponseDto;
import com.example.prello.comment.service.CommentService;
import com.example.prello.notification.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/boards/{boardId}/decks/{deckId}/cards/{cardId}/comments")
public class CommentController {

    private final CommentService commentService;

    private final SlackService slackService;

    //댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @PathVariable Long cardId,
            HttpServletRequest request,
            @Valid @RequestBody CommentRequestDto dto) {

        CommentResponseDto commentResponseDto = commentService.createComment(workspaceId, boardId, deckId, cardId, dto);
        slackService.sendSlackNotification("새 댓글이 작성되었습니다.", request, commentResponseDto.getContent());
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    //댓글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @PathVariable Long cardId,
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDto dto) {

        CommentResponseDto commentResponseDto = commentService.updateComment(workspaceId, boardId, deckId, cardId, id, dto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }


    //댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @PathVariable Long cardId,
            @PathVariable Long id
    ) {
        commentService.deleteComment(workspaceId, boardId, deckId, cardId, id);
        return new ResponseEntity<>("댓글이 삭제되었습니다", HttpStatus.OK);
    }
}
