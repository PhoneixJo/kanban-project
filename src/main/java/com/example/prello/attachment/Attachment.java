package com.example.prello.attachment;

import com.example.prello.card.entity.Card;
import com.example.prello.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder(builderClassName = "Builder", access = AccessLevel.PUBLIC)
@Table(name = "`attachment`")
public class Attachment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    private String fileName;

    private String fileUrl;

    private String fileType;

    public Attachment() {}

    public Attachment(Card card, String fileName, String fileUrl, String fileType) {
        this.card = card;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
    }
}
