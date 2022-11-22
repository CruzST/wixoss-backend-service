package com.wixossdeckbuilder.backendservice.model.dto;

import com.wixossdeckbuilder.backendservice.model.entities.Card;
import com.wixossdeckbuilder.backendservice.model.entities.DeckMetaData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class DeckAndCard {
    @ManyToOne
    @JoinColumn(name = "deck_id")
    DeckMetaData deck;

    @ManyToOne
    @JoinColumn(name = "card_id")
    Card card;
}
