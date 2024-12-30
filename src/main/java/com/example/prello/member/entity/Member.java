package com.example.prello.member.entity;

import com.example.prello.common.BaseEntity;
import com.example.prello.exception.CustomException;
import com.example.prello.member.auth.MemberAuth;
import com.example.prello.user.entity.User;
import com.example.prello.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "`member`")
@DynamicInsert
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="workspace_id", nullable=false)
    private Workspace workspace;

    @Column(nullable = false, columnDefinition = "varchar(20) default 'READ_ONLY'")
    @Enumerated(EnumType.STRING)
    private MemberAuth auth;

    public Member() {}

    public void updateMemberAuth(MemberAuth auth) {
        this.auth.validateAuthChange(auth);
        this.auth = auth;
    }
}
