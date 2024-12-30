package com.example.prello.attachment.controller;

import com.example.prello.attachment.dto.AttachmentForm;
import com.example.prello.attachment.dto.AttachmentResponseDto;
import com.example.prello.attachment.service.AttachmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    /**
     * 카드에 첨부 파일 등록
     *
     * @param form   첨부 파일 폼
     * @param cardId 카드 식별자
     * @return 201 CREATED
     */
    @PostMapping("/{cardId}")
    public ResponseEntity<AttachmentResponseDto> createAttachment(
            @Valid @ModelAttribute AttachmentForm form,
            @PathVariable Long cardId
    ) {

        AttachmentResponseDto attachmentResponseDto = attachmentService.createAttachment(form, cardId);
        return new ResponseEntity<>(attachmentResponseDto, HttpStatus.CREATED);
    }

    /**
     * 카드 내 첨부파일 조회
     *
     * @param cardId 카드 식별자
     * @return 200 OK
     */
    @GetMapping("/{cardId}")
    public ResponseEntity<List<AttachmentResponseDto>> findAttachment(
            @PathVariable Long cardId
    ) {

        List<AttachmentResponseDto> attachmentResponseDtos = attachmentService.findAttachment(cardId);
        return new ResponseEntity<>(attachmentResponseDtos, HttpStatus.OK);
    }

    /**
     * 첨부파일 다운로드
     *
     * @param id 첨부파일 식별자
     * @return 200 OK
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadAttachment(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        Resource resource = attachmentService.downloadAttachment(id);
        String contentDisposition = attachmentService.createContentDisposition(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        headers.setContentType(MediaType.parseMediaType(contentType));

        try {
            headers.setContentLength(resource.contentLength());
        } catch (IOException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * 첨부파일 삭제
     *
     * @param id 첨부파일 식별자
     * @return 200 OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttachment(
            @PathVariable Long id
    ) {

        attachmentService.deleteAttachment(id);
        return new ResponseEntity<>("첨부파일이 삭제되었습니다.", HttpStatus.OK);
    }
}
