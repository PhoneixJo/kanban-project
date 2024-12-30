package com.example.prello.comment.service;

import com.example.prello.board.service.BoardService;
import com.example.prello.card.entity.Card;
import com.example.prello.card.repository.CardRepository;
import com.example.prello.card.service.CardService;
import com.example.prello.comment.dto.CommentRequestDto;
import com.example.prello.comment.dto.CommentResponseDto;
import com.example.prello.comment.entity.Comment;
import com.example.prello.comment.repository.CommentRepository;
import com.example.prello.common.SessionName;
import com.example.prello.deck.service.DeckService;
import com.example.prello.exception.CommentErrorCode;
import com.example.prello.exception.CustomException;
import com.example.prello.user.entity.User;
import com.example.prello.user.service.UserService;
import com.example.prello.workspace.service.WorkspaceService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final WorkspaceService workspaceService;
    private final BoardService boardService;
    private final DeckService deckService;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final HttpSession session;

    //댓글 생성
    @Transactional
    public CommentResponseDto createComment(Long workspaceId, Long boardId, Long deckId, Long cardId, CommentRequestDto dto) {
        checkPathVariableIds(workspaceId, boardId, deckId, cardId);

        // 세션에서 userId 가져오기
        Long userId = (Long) session.getAttribute(SessionName.USER_ID);
        User user = userService.findByIdOrElseThrow(userId);

        Card card = cardRepository.findByIdOrElseThrow(cardId);

        Comment comment = Comment.builder()
                .content(dto.getContent())
                .name(user.getName())
                .user(user)
                .card(card)
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDto.toDto(savedComment);
    }


    //댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long workspaceId, Long boardId, Long deckId, Long cardId, Long id, CommentRequestDto dto) {
        checkPathVariableIds(workspaceId, boardId, deckId, cardId);

        Comment findComment = findByIdOrElseThrow(id);
        findComment.updateComment(dto.getContent());

        return CommentResponseDto.toDto(findComment);
    }


    //댓글 삭제
    @Transactional
    public void deleteComment(Long workspaceId, Long boardId, Long deckId, Long cardId, Long id) {
        checkPathVariableIds(workspaceId, boardId, deckId, cardId);
        Comment findComment = findByIdOrElseThrow(id);

        commentRepository.delete(findComment);
    }

    public List<Comment> findByCardId(Long id) {
        List<Comment> comments = commentRepository.findByCardIdOrderByCreatedAtDesc(id);
        return comments != null ? comments : Collections.emptyList();
    }


    //댓글 id로 조회
    public Comment findByIdOrElseThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CustomException(CommentErrorCode.COMMENT_NOT_FOUND));
    }

    //workspace, board 검증 및 board 반환
    private void checkPathVariableIds(Long workspaceId, Long boardId, Long deckId, Long cardId) {
        //workspace 검증
        workspaceService.findByIdOrElseThrow(workspaceId);

        //board 검증
        boardService.findByIdOrElseThrow(boardId);

        //deck 검증
        deckService.findByIdOrElseThrow(deckId);

        //card 검증
        cardRepository.findByIdOrElseThrow(cardId);
    }
}
