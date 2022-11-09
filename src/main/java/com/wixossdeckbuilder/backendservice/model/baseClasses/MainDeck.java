package com.wixossdeckbuilder.backendservice.model.baseClasses;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class MainDeck {
    Long deckId;
    String deckName;

    List<MainDeckContent> signiDeckContent;
    List<MainDeckContent> lrigDeckContent;
}
