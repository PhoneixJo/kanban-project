package com.example.prello.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum UserErrorCode implements ExceptionType{
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
    DELETED_ACCOUNT(HttpStatus.BAD_REQUEST, "탈퇴한 이메일은 사용할 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "찾을 수 없는 유저입니다."),
    ALREADY_LOGGED_IN(HttpStatus.BAD_REQUEST, "이미 로그인된 상태입니다."),
    NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND,"해당 이메일로 사용자를 찾을 수 없습니다: "),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "ADMIN 권한이 필요합니다.");


    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getName() {
        return this.name();
    }
}
