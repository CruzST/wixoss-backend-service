package com.wixossdeckbuilder.backendservice.model.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeckRequest {
    private Long deckOwnerId;
    private String deckName;
    private String description;
}
