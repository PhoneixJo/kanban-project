package com.example.prello.board.service;

import com.example.prello.board.dto.BoardRequestDto;
import com.example.prello.board.dto.BoardResponseDto;
import com.example.prello.board.entity.Board;
import com.example.prello.board.repository.BoardRepository;
import com.example.prello.card.dto.CardResponseDto;
import com.example.prello.card.entity.Card;
import com.example.prello.card.repository.CardRepository;
import com.example.prello.deck.dto.DeckResponseWithCardsDto;
import com.example.prello.deck.entity.Deck;
import com.example.prello.deck.repository.DeckRepository;
import com.example.prello.exception.BoardErrorCode;
import com.example.prello.exception.CustomException;
import com.example.prello.exception.DeckErrorCode;
import com.example.prello.workspace.entity.Workspace;
import com.example.prello.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceService workspaceService;
    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    // 보드 생성
    public BoardResponseDto createBoard(Long workspaceId, BoardRequestDto boardRequestDto) {
        Workspace workspace = workspaceService.findByIdOrElseThrow(workspaceId);

        Board board = new Board(boardRequestDto.getTitle(), boardRequestDto.getBgColor(), boardRequestDto.getBgImage(), workspace);
        boardRepository.save(board);
        return BoardResponseDto.builder()
                .workspaceId(board.getWorkspace().getId())
                .id(board.getId())
                .title(board.getTitle())
                .bgColor(board.getBg_color())
                .bgImage(board.getBg_image())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    // 보드 수정
    public BoardResponseDto updateBoard(Long workspaceId, Long id, BoardRequestDto boardRequestDto) {

        Board board = findByWorkspaceIdAndBoardIdOrElseThrow(workspaceId, id);

        Board update = board.update(boardRequestDto.getTitle(), boardRequestDto.getBgColor(), boardRequestDto.getBgImage());
        boardRepository.save(board);

        return BoardResponseDto.builder()
                .workspaceId(board.getWorkspace().getId())
                .id(board.getId())
                .title(board.getTitle())
                .bgColor(board.getBg_color())
                .bgImage(board.getBg_image())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    // 보드 상세 조회
    public BoardResponseDto getBoard(Long workspaceId, Long boardId) {
        Board board = findByWorkspaceIdAndBoardIdOrElseThrow(workspaceId, boardId);

        List<Deck> decks = deckRepository.findByBoardId(boardId);

        List<Card> cards = cardRepository.findCardsByWorkspaceIdAndBoardIdOrElseThrow(workspaceId, boardId);

        Map<Long, List<CardResponseDto>> cardsByDeck = cards.stream()
                .collect(Collectors.groupingBy(
                        card -> card.getDeck().getId(),
                        Collectors.mapping(CardResponseDto::toDto, Collectors.toList())
                ));

        List<DeckResponseWithCardsDto> deckDtos = decks.stream()
                .map(deck -> {
                    List<CardResponseDto> cardDtos = cardsByDeck.getOrDefault(deck.getId(), new ArrayList<>());
                    return DeckResponseWithCardsDto.toDto(deck, cardDtos);
                })
                .toList();

        // 6. BoardResponseDto로 변환하여 반환
        return BoardResponseDto.toDto(board, deckDtos);
    }

    // 보드 삭제
    public String deleteBoard(Long workspaceId, Long id) {
        findByWorkspaceIdAndBoardIdOrElseThrow(workspaceId, id);
        boardRepository.deleteById(id);
        return "보드가 삭제되었습니다.";
    }

    // 보드 findById
    public Board findByIdOrElseThrow(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    public Board findByWorkspaceIdAndBoardIdOrElseThrow(Long workspaceId, Long id) {
        return boardRepository.findByWorkspaceIdAndBoardId(workspaceId, id)
                .orElseThrow(() -> new CustomException(BoardErrorCode.BOARD_ROUTE_NOT_FOUND));
    }
}
