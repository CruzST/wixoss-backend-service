package com.wixossdeckbuilder.backendservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class Deck {
    Long deckId;
    String deckName;

    List<DeckContent> mainDeckContent;
    List<DeckContent> lrigDeckContent;
}
