package com.wixossdeckbuilder.backendservice.model.payloads;

import com.wixossdeckbuilder.backendservice.model.*;
import com.wixossdeckbuilder.backendservice.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardRequest {
    private String id;
    private String name;
    private List<Rarity> rarity;
    private CardType cardType;
    private LrigTypeOrClass lrigTypeOrClass;
    private ArrayList<Colors> colors;
    private int level;
    private ColorCost growCost;
    private ArrayList<ColorCost> cost;
    private String limit;
    private int power;
    private Team team;
    private Ability effects;
    private Ability lifeBurst;
    private String coin;
    private String setFormat;
    private Serial serial;

    // TODO: IMAGE ATTR GOES HERE
    // private Image image

    private String timing;

}
