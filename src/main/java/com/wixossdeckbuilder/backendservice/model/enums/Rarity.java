package com.wixossdeckbuilder.backendservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Rarity {
    PIECE("PI"),
    SUPER_RARE("SR"),
    LRIG("L"),
    LRIG_PROMO("L(P)"),
    RARE("R"),
    RARE_PROMO("R(P)"),
    RARE_PROMO_PRIME("R(P')"),
    COMMON("C"),
    COMMON_PROMO("C(P)"),
    STRUCTURE_DECK("ST"),
    STRUCTURE_DECK_PROMO("ST(P)"),
    PROMO("PR"),
    PROMO_PRIME("PR(P)"),
    ULTRA("???"),
    DIVA_RARE("DiR"),
    SECRET_RARE("SCR");

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
