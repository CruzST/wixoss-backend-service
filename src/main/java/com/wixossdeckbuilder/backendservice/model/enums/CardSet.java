package com.wixossdeckbuilder.backendservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CardSet {
    P00("INTERLUDE DIVA"),
    P01("GLOWING DIVA"),
    P02("CHANGING DIVA"),
    P03("STANDUP DIVA"),
    P04("VERTEX DIVA"),
    P05("CURIOSITY DIVA"),
    P06("WELCOME BACK DIVA ~selector~"),
    D01("DIVA DEBUT DECK ANCIENT SURPRISE"),
    D02("DIVA DEBUT DECK Nijisanji ver. Sanbaka"),
    D03("DIVA DEBUT DECK No Limit"),
    D04("DIVA DEBUT DECK Card Jockey"),
    D05("DIVA DEBUT DECK UCHU NO HAJIMARI"),
    D06("DIVA DEBUT DECK DIAGRAM"),
    D07("TOP DIVA DECK Deus Ex Machina"),
    D08("DIVA DEBUT DECK WHITE HOPE"),
    Promo("Promo");

    public final String value;

    public static String getAsString(String type) {
        for (CardSet set : CardSet.values()) {
            if (set.name().equalsIgnoreCase(type)) {
                return set.getValue();
            }
        }
        return null;
    }

    public static CardSet matchEnum(String type) {
        for (CardSet set : CardSet.values()) {
            if (set.name().equalsIgnoreCase(type)) {
                return set;
            }
        }
        return null;
    }

}
