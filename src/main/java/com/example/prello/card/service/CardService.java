package com.example.prello.card.service;

import com.example.prello.board.service.BoardService;
import com.example.prello.card.dto.*;
import com.example.prello.card.entity.Card;
import com.example.prello.card.repository.CardRepository;
import com.example.prello.comment.entity.Comment;
import com.example.prello.comment.repository.CommentRepository;
import com.example.prello.comment.service.CommentService;
import com.example.prello.deck.entity.Deck;
import com.example.prello.deck.service.DeckService;
import com.example.prello.user.entity.User;
import com.example.prello.user.service.UserService;
import com.example.prello.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    private final WorkspaceService workspaceService;
    private final BoardService boardService;
    private final DeckService deckService;
    private final UserService userService;
    private final CommentService commentService;
    private final CommentRepository commentRepository;


    /**
     * 카드 생성 서비스 메서드
     *
     * @return 새로 생성한 Card 내용의 CardResponseDto
     */
    @Transactional
    public CardResponseDto createCard(Long workspaceId, Long boardId, Long deckId, CardRequestDto dto) {
        checkPathVariable(workspaceId, boardId, deckId);
        Deck findDeck = deckService.findByIdOrElseThrow(deckId);

        Card card = Card.builder()
                .deck(findDeck)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .endAt(dto.getEndAt())
                .build();

        Card savedCard = cardRepository.save(card);

        return CardResponseDto.toDto(savedCard);
    }

    /**
     * 카드 (부분) 수정 서비스 메서드
     *
     * @param cardId 카드 식별자
     * @param dto    nullable
     * @return 수정한 Card 내용의 CardResponseDto
     */
    @Transactional
    public CardResponseDto updateCard(Long workspaceId, Long boardId, Long deckId, Long cardId, CardRequestDto dto) {
        checkPathVariable(workspaceId, boardId, deckId);
        Card findCard = cardRepository.findByIdOrElseThrow(cardId);
        findCard.updateCard(dto.getTitle(), dto.getDescription(), dto.getEndAt());

        return CardResponseDto.toDto(findCard);
    }

    /**
     * 카드 상세 조회 서비스 메서드
     *
     * @param id 카드 식별자
     * @return Comment 목록 포함한 Card dto
     */
    public CardDetailResponseDto findCard(Long workspaceId, Long boardId, Long deckId, Long id) {
        checkPathVariable(workspaceId, boardId, deckId);
        Card findCard = cardRepository.findByIdOrElseThrow(id);

        List<Comment> comments = commentRepository.findLatestCommentsByCardId(id);

        return new CardDetailResponseDto(findCard, comments);
    }

    public Page<CardSearchResponseDto> findBySearch(Long workspaceId, Long boardId, String title, String description,
                                                    LocalDateTime endAt, String assigneeName, Pageable pageable) {

        workspaceService.findByIdOrElseThrow(workspaceId);
        Page<Card> cardPage = cardRepository.findBySearch(boardId, title, description, endAt, assigneeName, pageable);

        if (!cardPage.getContent().isEmpty()) {
            Card firstCard = cardPage.getContent().get(0);
         }

        Page<CardSearchResponseDto> cards = cardPage
                .map(card -> new CardSearchResponseDto(card, commentService.findByCardId(card.getId())));

        return cards;
    }



    /**
     * 카드 삭제 서비스 메서드
     *
     * @param id 카드 식별자
     */
    @Transactional
    public void deleteCard(Long workspaceId, Long boardId, Long deckId, Long id) {
        checkPathVariable(workspaceId, boardId, deckId);
        Card findCard = cardRepository.findByIdOrElseThrow(id);

        cardRepository.delete(findCard);
    }

    /**
     * 카드 담당자 추가 서비스 메서드
     *
     * @param id 카드 식별자
     */
    @Transactional
    public void updateAssignees(Long workspaceId, Long boardId, Long deckId, Long id, CardAssigneesRequestDto dto) {
        checkPathVariable(workspaceId, boardId, deckId);
        Card findCard = cardRepository.findByIdOrElseThrow(id);

        User findUser = userService.findByIdOrElseThrow(dto.getUserId());

        findCard.addAssignees(findUser);
    }

    // TODO: 검증 로직 메서드화
    private void checkPathVariable(Long workspaceId, Long boardId, Long deckId) {
        workspaceService.findByIdOrElseThrow(workspaceId);
        boardService.findByIdOrElseThrow(boardId);
        deckService.findByIdOrElseThrow(deckId);
    }
}
