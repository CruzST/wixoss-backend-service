package com.wixossdeckbuilder.backendservice.model.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeckContext {

    DeckRequest deckRequest;
    DeckContentsRequest deckContentsRequest;
}
