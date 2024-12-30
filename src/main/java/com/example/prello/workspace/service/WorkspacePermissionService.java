package com.example.prello.workspace.service;

import com.example.prello.member.auth.MemberAuth;
import com.example.prello.security.session.SessionUtils;
import com.example.prello.workspace.dto.WorkspacePermissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspacePermissionService {

    private final SessionUtils sessionUtils;

    // 워크스페이스 소유자 권한 검증
    public void validateWorkspaceOwner(Long workspaceId, Long userId) {
        WorkspacePermissionDto permission = sessionUtils.getWorkspacePermission(workspaceId);

        if (permission == null) {
            throw new IllegalStateException("워크스페이스 권한 정보를 세션에서 찾을 수 없습니다. 워크스페이스 ID: " + workspaceId);
        }

        if (permission.getAuth() != MemberAuth.WORKSPACE) {
            throw new IllegalStateException("사용자는 워크스페이스 소유자가 아닙니다. 워크스페이스 ID: " + workspaceId);
        }
    }

    // 워크스페이스 접근 권한 검증
    public void validateWorkspaceAccess(Long workspaceId, Long userId, MemberAuth requiredAuth) {
        WorkspacePermissionDto permission = sessionUtils.getWorkspacePermission(workspaceId);

        if (permission == null) {
            throw new IllegalStateException("워크스페이스 권한 정보를 세션에서 찾을 수 없습니다. 워크스페이스 ID: " + workspaceId);
        }

        System.out.println("세션에 저장된 권한: " + permission.getAuth());
        System.out.println("검증할 권한: " + requiredAuth);

        if (!permission.getAuth().hasPermission(requiredAuth)) {
            throw new IllegalStateException("필요한 권한이 부족합니다. 워크스페이스 ID: " + workspaceId);
        }
    }
}