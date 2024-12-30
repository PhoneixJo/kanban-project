package com.example.prello.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ExceptionType {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 보드를 찾을 수 없습니다."),
    BOARD_ROUTE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 경로에서 보드를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
