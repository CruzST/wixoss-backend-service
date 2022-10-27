package com.wixossdeckbuilder.backendservice.model;

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
    D01("INTERLUDE DIVA"),
    D02("INTERLUDE DIVA"),
    D03("GLOWING DIVA"),
    D04("GLOWING DIVA"),
    D05("GLOWING DIVA"),
    D06("GLOWING DIVA"),
    D07("GLOWING DIVA"),
    D08("WELCOME BACK DIVA ~selector~");

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
