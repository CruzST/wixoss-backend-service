package com.wixossdeckbuilder.backendservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class Deck {
    Long deckId;
    String name;
    String owner;
    String description;
    LocalDate lastUpdated;


    List<DeckContent> mainDeckContent;
    List<DeckContent> lrigDeckContent;
}
