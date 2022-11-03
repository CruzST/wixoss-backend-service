package com.wixossdeckbuilder.backendservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardType {
    ASSIST_LRIG("ASSIST LRIG"),
    LRIG("LRIG"),
    PIECE("PIECE"),
    SIGNI("SIGNI"),
    SPELL("SPELL"),
    TOKEN("TOKEN");

    public final String value;

    public static CardType fromString(String type) {
        for (CardType cardType : CardType.values()) {
            if (cardType.getValue().equalsIgnoreCase(type)) {
                return cardType;
            }
        }
        return null;
    }

}
