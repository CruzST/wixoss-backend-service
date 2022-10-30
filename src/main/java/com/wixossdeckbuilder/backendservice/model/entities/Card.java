package com.wixossdeckbuilder.backendservice.model.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.wixossdeckbuilder.backendservice.model.*;
import com.wixossdeckbuilder.backendservice.model.enums.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cards")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs(
        {@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)}
)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Enumerated(EnumType.STRING)
    private List<Rarity> rarity; // Store as the .name()

    @Column(name = "card_type")
    @Enumerated(EnumType.STRING)
    private CardType cardType; // Store as the .name()

    @Column(name = "lrig_type_or_class")
    @Enumerated(EnumType.STRING)
    private LrigTypeOrClass lrigTypeOrClass; // Store as the .name()

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Enumerated(EnumType.STRING)
    private List<Colors> colors;

    private  int level;


    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "grow_cost")
    private ColorCost growCost;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "color_cost")
    private List<ColorCost> cost;

    @Column(name = "additional_limit")
    private String limit;

    private int power;

    @Enumerated(EnumType.STRING)
    private Team team;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Ability effects;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb", name = "life_burst")
    private Ability lifeBurst;

    private String coin;

    @Column(name = "set_format")
    private String setFormat;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Serial serial;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Image image;

    //TODO: Add an attribute for TIMING
    /*
    * Timing could probably be an enum array
    * Need to check the different terms they use, should all be the same
    *
    * */

    private String timing;






}
