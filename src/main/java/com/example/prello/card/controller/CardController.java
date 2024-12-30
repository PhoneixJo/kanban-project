package com.example.prello.card.controller;

import com.example.prello.card.dto.CardAssigneesRequestDto;
import com.example.prello.card.dto.CardDetailResponseDto;
import com.example.prello.card.dto.CardRequestDto;
import com.example.prello.card.dto.CardResponseDto;
import com.example.prello.card.service.CardService;
import com.example.prello.common.SessionName;
import com.example.prello.notification.SlackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/boards/{boardId}/decks/{deckId}/cards")
public class CardController {

    private final CardService cardService;

    private final SlackService slackService;

    /**
     * 카드 생성
     *
     * @param workspaceId 워크스페이스 식별자
     * @param boardId     보드 식별자
     * @param deckId      리스트 식별자
     * @param dto         {@link CardRequestDto}
     * @return 201 CREATED
     */
    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @Valid @RequestBody CardRequestDto dto
    ) {

        CardResponseDto cardResponseDto = cardService.createCard(workspaceId, boardId, deckId, dto);

        return new ResponseEntity<>(cardResponseDto, HttpStatus.CREATED);
    }

    /**
     * 카드 (부분) 수정
     *
     * @param id  카드 식별자
     * @param dto 수정할 내용이 담긴 nullable dto
     * @return 200 OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @PathVariable Long id,
            HttpServletRequest request,
            @SessionAttribute(name = SessionName.USER_ID) Long userId,
            @RequestBody CardRequestDto dto
    ) {

        CardResponseDto cardResponseDto = cardService.updateCard(workspaceId, boardId, deckId, id, dto);
        slackService.sendSlackNotification(userId, "카드에 변경사항이 있습니다.", request, "워크스페이스 id: " + workspaceId + cardResponseDto.getTitle());
        return new ResponseEntity<>(cardResponseDto, HttpStatus.OK);
    }

    /**
     * 카드 담당자 추가
     *
     * @param id  카드 식별자
     * @param dto 담당자 id 포함 request dto
     * @return 200 OK
     */
    @PatchMapping("/{id}/update-assignees")
    public ResponseEntity<String> updateAssignees(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @PathVariable Long id,
            @Valid @RequestBody CardAssigneesRequestDto dto
    ) {

        cardService.updateAssignees(workspaceId, boardId, deckId, id, dto);
        return new ResponseEntity<>("담당자가 추가되었습니다.", HttpStatus.OK);
    }

    /**
     * 카드 상세 조회
     *
     * @param id 카드 식별자
     * @return 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<CardDetailResponseDto> findCard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @PathVariable Long id
    ) {

        CardDetailResponseDto cardDetailResponseDto = cardService.findCard(workspaceId, boardId, deckId, id);
        return new ResponseEntity<>(cardDetailResponseDto, HttpStatus.OK);
    }

    /**
     * 카드 삭제
     *
     * @param id 카드 식별자
     * @return 200 OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @PathVariable Long deckId,
            @PathVariable Long id
    ) {

        cardService.deleteCard(workspaceId, boardId, deckId, id);
        return new ResponseEntity<>("카드가 삭제되었습니다.", HttpStatus.OK);
    }
}
