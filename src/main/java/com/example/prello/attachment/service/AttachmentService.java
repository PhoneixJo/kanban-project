package com.example.prello.attachment.service;

import com.example.prello.attachment.FileStore;
import com.example.prello.attachment.dto.AttachmentForm;
import com.example.prello.attachment.dto.AttachmentResponseDto;
import com.example.prello.attachment.entity.Attachment;
import com.example.prello.attachment.repository.AttachmentRepository;
import com.example.prello.card.entity.Card;
import com.example.prello.card.repository.CardRepository;
import com.example.prello.exception.AttachmentErrorCode;
import com.example.prello.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final FileStore fileStore;
    private final CardRepository cardRepository;

    /**
     * 첨부파일 등록 서비스 메서드
     *
     * @param form   첨부파일 포함된 정보 폼
     * @param cardId 카드 식별자
     * @return 첨부파일 정보
     */
    @Transactional
    public AttachmentResponseDto createAttachment(AttachmentForm form, Long cardId) {
        Attachment attachment;
        try {
            String storeFileName = fileStore.storeFile(form.getAttachFile());

            String fileUrl = fileStore.getDestinationFileUrl() + "/" + storeFileName;

            attachment = Attachment.builder()
                    .uploadFileName(form.getFileName())
                    .storeFileName(storeFileName)
                    .fileUrl(fileUrl)
                    .fileType(fileStore.findExt(form.getAttachFile().getOriginalFilename()))
                    .build();
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new CustomException(AttachmentErrorCode.ATTACHMENT_NOT_INCLUDED);
        }

        Attachment savedAttachment = attachmentRepository.save(attachment);
        // 카드에 첨부
        addAttachmentToCard(cardId, savedAttachment.getId());

        return AttachmentResponseDto.toDto(savedAttachment);
    }

    /**
     * 카드 내 첨부파일 조회
     *
     * @param cardId 카드 식별자
     * @return 카드 내 첨부파일 목록
     */
    public List<AttachmentResponseDto> findAttachment(Long cardId) {
        List<Attachment> attachments = attachmentRepository.findAllByCardId(cardId);
        return attachments.stream().map(AttachmentResponseDto::toDto).toList();
    }

    /**
     * 첨부파일 다운로드
     *
     * @param id 첨부파일 식별자
     * @return 첨부파일 리소스
     */
    public Resource downloadAttachment(Long id) {
        Attachment findAttachment = attachmentRepository.findByIdOrElseThrow(id);
        String storeFileName = findAttachment.getStoreFileName();
        return new FileSystemResource(new File(fileStore.getDestinationFileUrl(), storeFileName));
    }

    /**
     * 첨부파일 삭제
     *
     * @param id 첨부파일 식별자
     */
    @Transactional
    public void deleteAttachment(Long id) {
        Attachment findAttachment = attachmentRepository.findByIdOrElseThrow(id);
        attachmentRepository.delete(findAttachment);
    }

    /**
     * 첨부파일 다운로드 헤더 설정용 content disposition 생성
     *
     * @param id 첨부파일 식별자
     * @return content disposition 문자열
     */
    public String createContentDisposition(Long id) {
        Attachment findAttachment = attachmentRepository.findByIdOrElseThrow(id);
        String encodedName = URLEncoder.encode(findAttachment.getUploadFileName(), StandardCharsets.UTF_8)
                .replace("+", "%20");
        return "attachment; filename=\"" + encodedName + "\"";
    }

    /**
     * 카드에 첨부파일을 등록
     *
     * @param cardId       카드 식별자
     * @param attachmentId 첨부파일 식별자
     */
    private void addAttachmentToCard(Long cardId, Long attachmentId) {
        Card findCard = cardRepository.findByIdOrElseThrow(cardId);
        Attachment findAttachment = attachmentRepository.findByIdOrElseThrow(attachmentId);

        findAttachment.addAttachmentToCard(findCard);
    }
}
