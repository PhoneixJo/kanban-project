package com.example.prello.member.repository;

import com.example.prello.member.auth.MemberAuth;
import com.example.prello.member.entity.Member;
import com.example.prello.workspace.dto.WorkspacePermissionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m JOIN m.user WHERE m.id = :id")
    Optional<Member> findByMemberIdWithUser(
            @Param("id") Long id);

    @Query("SELECT m.auth FROM Member m WHERE m.user.id = :userId AND m.workspace.id = :workspaceId")
    Optional<MemberAuth> findMemberAuthByUserIdAndWorkspaceId(
            @Param("userId") Long userId,
            @Param("workspaceId") Long workspaceId);

    boolean existsByUserIdAndWorkspaceId(Long userId, Long workspaceId);

    @Query("SELECT m FROM Member m WHERE m.workspace.id = :workspaceId")
    List<Member> findByWorkspaceId(
            @Param("workspaceId") Long workspaceId);

    @Query("SELECT new com.example.prello.workspace.dto.WorkspacePermissionDto(m.workspace.id, m.auth) " +
            "FROM Member m WHERE m.user.id = :userId")
    List<WorkspacePermissionDto> findWorkspacePermissionsByUserId(@Param("userId") Long userId);
}
