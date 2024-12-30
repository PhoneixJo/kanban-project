package com.example.prello.workspace.service;

import com.example.prello.common.SessionName;
import com.example.prello.member.auth.MemberAuth;
import com.example.prello.security.session.SessionUtils;
import com.example.prello.workspace.dto.WorkspacePermissionDto;
import com.example.prello.workspace.dto.WorkspaceRequestDto;
import com.example.prello.workspace.dto.WorkspaceResponseDto;
import com.example.prello.workspace.entity.Workspace;
import com.example.prello.workspace.repository.WorkspaceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final SessionUtils sessionUtils;

    // 워크스페이스 생성
    @Transactional
    public WorkspaceResponseDto createWorkspace(@Valid WorkspaceRequestDto workspaceRequestDto) {
        Workspace workspace = Workspace.builder()
                .title(workspaceRequestDto.getTitle())
                .description(workspaceRequestDto.getDescription())
                .build();
        Workspace createWorkspace = workspaceRepository.save(workspace);

        WorkspacePermissionDto permission = new WorkspacePermissionDto(createWorkspace.getId(), MemberAuth.WORKSPACE);
        sessionUtils.addWorkspacePermission(permission);

        return WorkspaceResponseDto.toDto(createWorkspace);
    }

    // 워크스페이스 수정
    // todo 워크스페이스 소유자만 워크스페이스 수정가능하게 로직 수정 필요
    @Transactional
    public WorkspaceResponseDto updateWorkspace(Long id, @Valid WorkspaceRequestDto workspaceRequestDto) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스가 존재하지 않습니다."));
        workspace.update(workspaceRequestDto.getTitle(), workspaceRequestDto.getDescription());
        return WorkspaceResponseDto.toDto(workspace);
    }

    // 워크스페이스 목록 조회
    public List<WorkspaceResponseDto> getAllWorkspaces() {
        List<Workspace> workspaces = workspaceRepository.findAll();
        return workspaces.stream()
                .map(WorkspaceResponseDto::toDto)
                .toList();
    }

    // 워크스페이스 상세 조회
    public WorkspaceResponseDto getWorkspace(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스가 존재하지 않습니다."));
        return WorkspaceResponseDto.toDto(workspace);
    }

    // 워크스페이스 삭제
    public String deleteWorkspace(Long id) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스가 존재하지 않습니다."));

        sessionUtils.removeWorkspacePermission(id);

        workspaceRepository.delete(workspace);

        return "워크스페이스가 삭제되었습니다";
    }

    public Workspace findByIdOrElseThrow(Long id) {
        return workspaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(""));
    }
}
