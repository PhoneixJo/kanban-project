package com.example.prello.member.service;

import com.example.prello.member.auth.MemberAuth;
import com.example.prello.member.repository.MemberRepository;
import com.example.prello.member.dto.MemberRequestDto;
import com.example.prello.member.dto.MemberResponseDto;
import com.example.prello.member.entity.Member;
import com.example.prello.workspace.service.WorkspacePermissionService;
import com.example.prello.security.session.SessionUtils;
import com.example.prello.user.entity.User;
import com.example.prello.user.service.UserService;
import com.example.prello.workspace.dto.WorkspacePermissionDto;
import com.example.prello.workspace.entity.Workspace;
import com.example.prello.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorkspaceService workspaceService;
    private final UserService userService;
    private final SessionUtils sessionUtils;
    private final WorkspacePermissionService workspacePermissionService;


    // 멤버권한 변경
    // todo 세션에서 유저정보 확인 후 권한 체크 필요
    // todo 인증 인가 로직 수정 필요 (세션 사용필요)
    @Transactional
    public MemberResponseDto updateMemberAuth(Long workspaceId, Long id, MemberRequestDto memberRequestDto) throws IllegalAccessException {
        Workspace workspace = workspaceService.findByIdOrElseThrow(workspaceId);

        Long userId = sessionUtils.getLoginUserId();
        workspacePermissionService.validateWorkspaceOwner(workspaceId, userId);

        Member member = findByIdWithUserOrElseThrow(id);
        MemberAuth currentAuth = member.getAuth();
        member.updateMemberAuth(memberRequestDto.getAuth());

        sessionUtils.updateWorkspacePermission(id, MemberAuth.WORKSPACE);

        return MemberResponseDto.toDto(member);
    }

    // 워크스페이스 멤버 추가
    // todo 세션에서 유저정보 확인 후 권한 체크 필요
    @Transactional
    public String addWorkspaceMember(Long workspaceId, @Valid MemberRequestDto memberRequestDto) {
        Long userId = sessionUtils.getLoginUserId();

        workspacePermissionService.validateWorkspaceOwner(workspaceId, userId);

        Workspace workspace = workspaceService.findByIdOrElseThrow(workspaceId);

        User user = userService.findUserByEmailOrElseThrow(memberRequestDto.getEmail());

        boolean isAlreadyMember = memberRepository.existsByUserIdAndWorkspaceId(user.getId(), workspaceId);
        if (isAlreadyMember) {
            throw new IllegalStateException("이미 워크스페이스의 멤버입니다.");
        }

        Member member = Member.builder()
                .workspace(workspace)
                .user(user)
                .auth(memberRequestDto.getAuth())
                .build();

        memberRepository.save(member);

        sessionUtils.updateWorkspacePermission(userId, MemberAuth.WORKSPACE);

        return "멤버가 추가되었습니다." + user.getEmail();
    }

    // 워크스페이스 멤버 조회
    // todo 세션에서 유저정보 확인 후 권한 체크 필요
    public List<MemberResponseDto> getWorkspaceMembers(Long workspaceId) {
        Workspace workspace = workspaceService.findByIdOrElseThrow(workspaceId);
        Long userId = sessionUtils.getLoginUserId();
        workspacePermissionService.validateWorkspaceAccess(workspaceId, userId, MemberAuth.READ_ONLY);

        List<Member> workspaceMember = getMembersByWorkspaceId(workspaceId);
        return workspaceMember.stream()
                .map(MemberResponseDto::toDto)
                .toList();
    }

    // member id 확인
    public Member findMemberByIdOrElseThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    // member id, user id 함께 확인 (fetch Join)
    private Member findByIdWithUserOrElseThrow(Long id) {
        return memberRepository.findByMemberIdWithUser(id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    private List<Member> getMembersByWorkspaceId(Long workspaceId) {
        return memberRepository.findByWorkspaceId(workspaceId);
    }

    // 사용자와 관련된 워크스페이스 권한 정보를 반환
    public List<WorkspacePermissionDto> getWorkspacePermissionsByUserId(Long userId) {
        return memberRepository.findWorkspacePermissionsByUserId(userId);
    }
}
