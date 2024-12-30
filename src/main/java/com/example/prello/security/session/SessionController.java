package com.example.prello.security.session;

import com.example.prello.workspace.dto.WorkspacePermissionDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SessionController {

    private final SessionUtils sessionUtils;

    @GetMapping("/session")
    public String session(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "세션이 없습니다. 로그인해주세요.";
        }

        // session 정보 조회
        log.info("session.getId()={}", session.getId());
        log.info("session.getMaxInactiveInterval()={}", session.getMaxInactiveInterval());
        log.info("session.getCreationTime()={}", session.getCreationTime());
        log.info("session.getLastAccessedTime()={}", session.getLastAccessedTime());
        log.info("session.isNew()={}", session.isNew());

        return "세션이 정상적으로 조회되었습니다.";
    }

    @GetMapping("/session/workspace")
    public ResponseEntity<List<WorkspacePermissionDto>> getWorkspacePermissions() {
        List<WorkspacePermissionDto> permissions = sessionUtils.getWorkspacePermissions();
        return ResponseEntity.ok(permissions);
    }
}