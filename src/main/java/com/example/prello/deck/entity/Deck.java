package com.example.prello.deck.entity;

import com.example.prello.board.entity.Board;
import com.example.prello.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "`deck`")
public class Deck extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "`order`", nullable = false)
    private int order;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public Deck() {
    }

    public Deck(String title, int order, Board board) {
        this.title = title;
        this.order = order;
        this.board = board;
    }

    //제목 업데이트
    public void updateDeckTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    //순서 업데이트
    public void updateDeckOrder(int newOrder) {
        if (order >= 0) {
            this.order = newOrder;
        }
    }
}
