package com.example.prello.board.entity;

import com.example.prello.common.BaseEntity;
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
@Table(name = "`board`")
@DynamicInsert
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    private String title;

    @Column(nullable = false, columnDefinition = "varchar(7) default '#ffffff'")
    private String bg_color;

    private String bg_image;

    public Board() {
    }

    public Board(String title, String bg_color, String bg_image, Workspace workspace) {
        this.title = title;
        this.bg_color = bg_color;
        this.bg_image = bg_image;
        this.workspace = workspace;
    }

    public Board update(String title, String bg_color, String bg_image) {
        if (title != null) {
            this.title = title;
        }
        if (bg_color != null) {
            this.bg_color = bg_color;
        }
        if (bg_image != null) {
            this.bg_image = bg_image;
        }
        return this;
    }
}
