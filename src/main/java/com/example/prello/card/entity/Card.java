package com.example.prello.card.entity;

import com.example.prello.common.BaseEntity;
import com.example.prello.deck.entity.Deck;
import com.example.prello.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "`card`")
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "deck_id")
    private Deck deck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 10)
    @Size(max = 10, message = "카드 제목은 10자 이내여야 합니다.")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime endAt;

    public Card(){}

    /**
     * 담당자 추가
     *
     * @param user 담당자(멤버)
     */
    public void addAssignees(User user) {
        this.user = user;
    }

    /**
     * 카드 업데이트 메서드. null 이 아니면 값 변경.
     *
     * @param title 변경할 제목
     * @param description 변경할 설명
     * @param endAt 변경할 마감일
     */
    public void updateCard(String title, String description, LocalDateTime endAt) {
        if(title != null) {
            this.title = title;
        }

        if(description != null) {
            this.description = description;
        }

        if(endAt != null) {
            this.endAt = endAt;
        }
    }
}
