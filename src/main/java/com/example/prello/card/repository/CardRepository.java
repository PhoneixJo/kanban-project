package com.example.prello.card.repository;

import com.example.prello.card.entity.Card;
import com.example.prello.exception.CardErrorCode;
import com.example.prello.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * id로 단일 카드를 찾고, 없을 시 커스텀 예외
     *
     * @param id 카드 식별자
     * @return 카드
     */
    default Card findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(CardErrorCode.CARD_NOT_FOUND));
    }

    @Query("SELECT c FROM Card c " +
            "LEFT JOIN FETCH c.deck d " +
            "LEFT JOIN FETCH d.board b " +
            "WHERE b.id = :boardId AND b.workspace.id = :workspaceId")
    List<Card> findCardsByWorkspaceIdAndBoardId(@Param("workspaceId") Long workspaceId,
                                                @Param("boardId") Long boardId);

    default List<Card> findCardsByWorkspaceIdAndBoardIdOrElseThrow(Long workspaceId, Long boardId) {
        return findCardsByWorkspaceIdAndBoardId(workspaceId, boardId);
    }

    @Query("SELECT DISTINCT c FROM Card c " +
            "JOIN c.deck d " +
            "JOIN d.board b " +
            "LEFT JOIN c.user u " +
            "WHERE (:boardId IS NULL OR b.id = :boardId) " +
            "AND (:title IS NULL OR c.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:description IS NULL OR c.description LIKE CONCAT('%', :description, '%')) " +
            "AND (:endAt IS NULL OR c.endAt <= :endAt) " +
            "AND (:userName IS NULL OR u.name LIKE CONCAT('%', :userName, '%'))")
    Page<Card> findBySearch(
            @Param("boardId") Long boardId,
            @Param("title") String title,
            @Param("description") String description,
            @Param("endAt") LocalDateTime endAt,
            @Param("userName") String assigneeName,
            Pageable pageable
    );
}
