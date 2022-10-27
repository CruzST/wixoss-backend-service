package com.wixossdeckbuilder.backendservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
*
* The Ability Class is actually for both "effects" of a card and the "Life bursts" of a card
*
* */
@AllArgsConstructor
enum AbilityType {
    CARD_EFFECT("card_effect"),
    LIFE_BURST("life_burst");

    public final String value;
}


@Setter
@Getter
public class Ability {
    private AbilityType abilityType;
    private String[] abilityText;

    public Ability(boolean cardEffect, String[] text) {
         this.abilityType = cardEffect ? AbilityType.CARD_EFFECT : AbilityType.LIFE_BURST;
         this.abilityText = text;
    }
}
