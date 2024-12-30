package com.example.prello.comment.repository;

import com.example.prello.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 카드 id로 댓글 목록 불러오기
     *
     * @param cardId 카드 식별자
     * @return 해당 카드에 작성된 댓글 목록
     */
    @Query(
            "SELECT co FROM Comment co "
                    + "JOIN FETCH co.card ca "
                    + "WHERE ca.id = :cardId "
                    + "ORDER BY co.createdAt"
    )
    List<Comment> findLatestCommentsByCardId(@Param("cardId") Long cardId);

    List<Comment> findByCardIdOrderByCreatedAtDesc(Long cardId);
}
