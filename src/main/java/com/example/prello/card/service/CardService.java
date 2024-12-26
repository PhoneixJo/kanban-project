package com.example.prello.card.service;

import com.example.prello.card.dto.CardAssigneesRequestDto;
import com.example.prello.card.dto.CardDetailResponseDto;
import com.example.prello.card.dto.CardRequestDto;
import com.example.prello.card.dto.CardResponseDto;
import com.example.prello.card.entity.Card;
import com.example.prello.card.repository.CardRepository;
import com.example.prello.comment.Comment;
import com.example.prello.deck.entity.Deck;
import com.example.prello.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    /**
     * 카드 생성 서비스 메서드
     *
     * @return 새로 생성한 Card 내용의 CardResponseDto
     */
    @Transactional
    public CardResponseDto createCard(Long workspaceId, Long boardId, Long deckId, CardRequestDto dto) {
        checkPathVariable(workspaceId, boardId, deckId);
        // TODO: 유의미한 findDeck 로 수정
        Deck findDeck = new Deck();

//        // TODO - 회의: 서비스 방식
//        if (dto.getEndAt().isBefore(LocalDateTime.now())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "마감일은 현재 시간 이전일 수 없습니다.");
//        }

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
        Card findCard = findCardByIdOrElseThrow(cardId);
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
        Card findCard = findCardByIdOrElseThrow(id);

        // TODO: 댓글 찾기
        List<Comment> comments = new ArrayList<>();

        return new CardDetailResponseDto(findCard, comments);
    }

    /**
     * 카드 삭제 서비스 메서드
     *
     * @param id 카드 식별자
     */
    @Transactional
    public void deleteCard(Long workspaceId, Long boardId, Long deckId, Long id) {
        checkPathVariable(workspaceId, boardId, deckId);
        Card findCard = findCardByIdOrElseThrow(id);

        // TODO - 회의: 논리 삭제?
    }

    /**
     * 카드 담당자 추가 서비스 메서드
     *
     * @param id 카드 식별자
     */
    @Transactional
    public void updateAssignees(Long workspaceId, Long boardId, Long deckId, Long id, CardAssigneesRequestDto dto) {
        checkPathVariable(workspaceId, boardId, deckId);
        Card findCard = findCardByIdOrElseThrow(id);

        // TODO: 유의미한 User 로 변경
        // userService.findUserByIdOrElseThrow(dto.getUserId());
        User user = new User();

        findCard.addAssignees(user);
    }

    /**
     * Card id로 Card 를 찾아 반환, 없을 시 Throw
     *
     * @param id 카드 식별자
     * @return id에 해당하는 Card
     */
    public Card findCardByIdOrElseThrow(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    // TODO: 검증 로직 메서드화
    private void checkPathVariable(Long workspaceId, Long boardId, Long deckId) {
        // TODO: 해당 service 단의 find 메서드로 바꾸기
        //  workspaceId 검증
        //  boardId 검증
        //  deckId 검증
    }
}
