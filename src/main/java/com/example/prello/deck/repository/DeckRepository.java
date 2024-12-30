package com.example.prello.deck.repository;

import com.example.prello.deck.entity.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck, Long> {

    @Query("SELECT d FROM Deck d WHERE d.board.id = :boardId AND d.order BETWEEN :startOrder AND :endOrder")
    List<Deck> findDecksInOrderRange(
            @Param("boardId") Long boardId,
            @Param("startOrder") int startOrder,
            @Param("endOrder") int endOrder);

    List<Deck> findByBoardId(Long boardId);

    @Query("SELECT MAX(d.order) FROM Deck d WHERE d.board.id = :boardId")
    Optional<Integer> findMaxOrderByBoardId(@Param("boardId") Long boardId);
}
