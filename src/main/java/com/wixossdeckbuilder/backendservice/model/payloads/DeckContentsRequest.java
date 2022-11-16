package com.wixossdeckbuilder.backendservice.model.payloads;

import com.wixossdeckbuilder.backendservice.model.dto.DeckCards;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DeckContentsRequest {

    Long deckId;

    List<DeckCards> signiDeck;

    // Lrig deck can only have 1 of a card so only need serials in the payload
    List<String> lrigDeck;

    public void setDeckId(Long deckId) {
        this.deckId = deckId;
    }
}
