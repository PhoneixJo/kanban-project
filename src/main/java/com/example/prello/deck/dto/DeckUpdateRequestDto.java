package com.example.prello.deck.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DeckUpdateRequestDto {

    @Size(max = 20, message = "20자 이내로 작성해야 합니다.")
    private String title;

    private int order;

}
