package com.example.prello.comment.entity;

import com.example.prello.card.entity.Card;
import com.example.prello.common.BaseEntity;

import com.example.prello.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import static com.example.prello.comment.entity.QComment.comment;

@Entity
@Getter
@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "`comment`")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public Comment() {}

    public Comment( String name, String content, User user, Card card) {
        this.name = name;
        this.content = content;
        this.user = user;
        this.card = card;
    }

    public void updateComment(String content) {
        if(content != null) {
            this.content = content;
        }
    }
}
