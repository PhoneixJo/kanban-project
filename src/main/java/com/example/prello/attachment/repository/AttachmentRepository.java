package com.example.prello.attachment.repository;

import com.example.prello.attachment.entity.Attachment;
import com.example.prello.exception.AttachmentErrorCode;
import com.example.prello.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    default Attachment findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(AttachmentErrorCode.ATTACHMENT_NOT_FOUND));
    }

    /**
     * 카드에 첨부된 모든 첨부파일을 찾음
     *
     * @param cardId 카드 식별자
     * @return 첨부파일 목록
     */
    @Query("SELECT a FROM Attachment a JOIN FETCH a.card c WHERE c.id = :cardId")
    List<Attachment> findAllByCardId(Long cardId);
}
