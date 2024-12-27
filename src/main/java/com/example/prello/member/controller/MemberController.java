package com.example.prello.member.controller;

import com.example.prello.member.dto.MemberRequestDto;
import com.example.prello.member.dto.MemberResponseDto;
import com.example.prello.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/members")
public class MemberController {

    private final MemberService memberService;

    //멤버 권한 변경
    @PatchMapping("/{id}")
    public ResponseEntity<MemberResponseDto> updateMemberAuth(
            @PathVariable Long workspaceId,
            @PathVariable Long id,
            @RequestBody MemberRequestDto memberRequestDto
    ) throws IllegalAccessException {
        MemberResponseDto updatedMemberAuth = memberService.updateMemberAuth(workspaceId, id, memberRequestDto);
        return new ResponseEntity<>(updatedMemberAuth, HttpStatus.OK);
    }

    // 워크스페이스 멤버 추가
    @PostMapping
    public ResponseEntity<String> addWorkspaceMember(
            @PathVariable Long workspaceId,
            @Valid @RequestBody MemberRequestDto memberRequestDto) {
        String message = memberService.addWorkspaceMember(workspaceId, memberRequestDto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // 워크스페이스 멤버 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getWorkspaceMembers(
            @PathVariable Long workspaceId) {
        List<MemberResponseDto> MemberResponseDtoList = memberService.getWorkspaceMembers(workspaceId);
        return new ResponseEntity<>(MemberResponseDtoList, HttpStatus.OK);
    }
}
