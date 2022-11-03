package com.wixossdeckbuilder.backendservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Rarity {
    STRUCTURE_DECK("ST"),
    LRIG("L"),
    PIECE("PI"),
    COMMON("C"),
    RARE("R"),
    SECRET_RARE("SCR"),
    SUPER_RARE("SR"),
    DIVA_RARE("DiR"),
    ULTRA("???"),
    STRUCTURE_DECK_PROMO("ST(P)"),
    LRIG_PROMO("L(P)"),
    COMMON_PROMO("C(P)"),
    RARE_PROMO("R(P)"),
    RARE_PROMO_PRIME("R(P')"),
    PROMO("PR"),
    PROMO_PRIME("PR(P)"),
    TOKEN("TOKEN");

    public final String value;

    public static Rarity fromString(String type) {
        for (Rarity rarity : Rarity.values()) {
            if (rarity.getValue().equalsIgnoreCase(type)) {
                return rarity;
            }
        }
        return null;
    }


}
