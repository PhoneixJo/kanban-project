package com.example.prello.member.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPermissionService {

    public void workspacePermit(MemberAuth memberAuth) throws IllegalAccessException {
        if (memberAuth == MemberAuth.WORKSPACE) {
            throw new IllegalAccessException("READ_ONLY 권한 사용자는 이 작업을 수행할 수 없습니다.");
        }
    }

    public void boardPermit(MemberAuth memberAuth) throws IllegalAccessException {
        if (memberAuth == MemberAuth.BOARD) {
            throw new IllegalAccessException("READ_ONLY 권한 사용자는 이 작업을 수행할 수 없습니다.");
        }
    }

    public void readOnlyPermit(MemberAuth memberAuth) throws IllegalAccessException {
        if (memberAuth == MemberAuth.READ_ONLY) {
            throw new IllegalAccessException("READ_ONLY 권한 사용자는 이 작업을 수행할 수 없습니다.");
        }
    }
}
