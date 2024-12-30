package com.example.prello.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ExceptionType {
    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 워크스페이스의 멤버입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 멤버를 찾을 수 없습니다."),
    WRONG_AUTH_STRING(HttpStatus.BAD_REQUEST, "잘못된 권한 요청입니다."),
    AUTH_EMPTY(HttpStatus.BAD_REQUEST, "권한은 필수 입력값입니다."),
    INVALID_AUTH(HttpStatus.BAD_REQUEST, "해당하는 권한을 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    ACCESS_FORBIDDEN(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 워크스페이스를 찾을 수 없습니다."),
    ALREADY_WORKSPACE_MEMBER(HttpStatus.CONFLICT, "이미 워크스페이스의 멤버입니다."),
    NULL_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 null이거나 비어 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
