package com.example.prello.member.auth;

import com.example.prello.exception.CustomException;

import com.example.prello.exception.MemberErrorCode;
import lombok.Getter;

@Getter
public enum MemberAuth {
    WORKSPACE("workspace") {
        @Override
        public void validateAuthChange(MemberAuth newAuth) {
        }
    },
    BOARD("board") {
        @Override
        public void validateAuthChange(MemberAuth newAuth) {
        }
    },
    READ_ONLY("read-only") {
        @Override
        public void validateAuthChange(MemberAuth newAuth) {
        }
    };

    public abstract void validateAuthChange(MemberAuth newAuth);

    private final String memberAuth;

    MemberAuth(String auth) {
        this.memberAuth = auth;
    }

    public static MemberAuth of(String authName) {
        for (MemberAuth auth : MemberAuth.values()) {
            if (auth.getMemberAuth().equals(authName)) {
                return auth;
            }
        }
        throw new CustomException(MemberErrorCode.INVALID_AUTH);
    }

    public boolean hasPermission(MemberAuth requiredAuth) {
        // 권한 비교 로직
        if (this == WORKSPACE) return true; // 최상위 권한
        if (this == BOARD && requiredAuth == READ_ONLY) return true; // BOARD는 READ_ONLY 허용
        return this == requiredAuth;
    }
}