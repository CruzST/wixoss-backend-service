package com.wixossdeckbuilder.backendservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Team {
    ANCIENT_SURPRISE("Ancient Surprise"),
    CARD_JOCKEY("Card Jockey"),
    DXM("DEUS・EX・MACHINA"),
    DIAGRAM("DIAGRAM"),
    NO_LIMIT("No Limit"),
    SANBAKA("Sanbaka"),
    UCHU("UCHU NO HAJIMARI"),
    NONE("none");
    // TODO: Add new ones

    public final String value;

    public static Team fromString(String type) {
        for (Team cardType : Team.values()) {
            if (cardType.getValue().equalsIgnoreCase(type)) {
                return cardType;
            }
        }
        return null;
    }

}
