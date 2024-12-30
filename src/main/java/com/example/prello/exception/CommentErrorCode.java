package com.example.prello.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ExceptionType {

    COMMENT_ERROR_CODE(HttpStatus.NOT_FOUND, "해당 댓글을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}

