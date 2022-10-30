package com.wixossdeckbuilder.backendservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LrigTypeOrClass {
    IT_ANGEL("Idol Tone:Angel"),
    IT_BRAVE("Idol Tone:Brave"),
    IT_DEMON("Idol Tone:Demon"),
    IT_WISDOM("Idol Tone:Wisdom"),
    IT_BEAUTY("Idol Tone:Beauty"),
    WT_VENOM("War Tone:Venom"),
    WT_ARMED("War Tone:Armed"),
    WT_AMUSER("War Tone:Amuser"),
    WT_TRICKSTER("War Tone:Trickster"),
    NT_COSMOS("Nature Tone:Cosmo"),
    NT_ATOM("Nature Tone:Atom"),
    NT_JEWEL("Nature Tone:Jewel"),
    NT_PLANT("Nature Tone:Plant"),
    NT_BACTERIA("Nature Tone:Bacteria"),
    MT_COOKING("Mech Tone:Cooking"),
    MT_LEGACY("Mech Tone:Legacy"),
    MT_ELECTRONICS("Mech Tone:Electronics"),
    MT_VIRTUAL("Mech Tone:Virtual"),
    LT_DRAGON("Living Tone:Dragon"),
    LT_OUTCAST("Living Tone:Outcast"),
    LT_TERRABEAST("Living Tone:Terra Beast"),
    LT_AQUATICBEAST("Living Tone:Aquatic Beast"),
    LT_AERIALBEAST("Living Tone:Aerial Beast"),
    LT_INSECT("Living Tone:Insect"),
    PRIMAL_TONE("Primal Tone"),
    AKINO("Akino"),
    ANGE("Ange"),
    AT("At"),
    BANG("Bang"),
    DEUS("Deus"),
    EX("Ex"),
    HIRANA("Hirana"),
    LION("LION"),
    LIZE("Lize"),
    LOVIT("LOVIT"),
    MACHINA("Machina"),
    MADOKA("Madoka"),
    MUZICA("Muzica"),
    NOVA("NOVA"),
    REI("Rei"),
    SANGA("Sanga"),
    TAMAGO("Tamago"),
    TAWIL("Tawil"),
    TOKO("Toko"),
    UMR("Umr"),
    WOLF("WOLF");
    //TODO: Add new ones
    public final String value;

    public static LrigTypeOrClass fromString(String type) {
        for (LrigTypeOrClass cardType : LrigTypeOrClass.values()) {
            if (cardType.getValue().equalsIgnoreCase(type)) {
                return cardType;
            }
        }
        return null;
    }

}
