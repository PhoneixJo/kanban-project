package com.example.prello.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AttachmentErrorCode implements ExceptionType {
    ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 첨부파일을 찾을 수 없습니다."),
    ATTACHMENT_NOT_INCLUDED(HttpStatus.NOT_FOUND, "첨부할 파일 정보가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
