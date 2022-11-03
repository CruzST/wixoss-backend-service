package com.wixossdeckbuilder.backendservice.model.interfaces;

import com.wixossdeckbuilder.backendservice.model.entities.Card;
import com.wixossdeckbuilder.backendservice.model.entities.Deck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class DeckAndCard {
    @ManyToOne
    @JoinColumn(name = "deck_id")
    Deck deck;

    @ManyToOne
    @JoinColumn(name = "card_id")
    Card card;
}
