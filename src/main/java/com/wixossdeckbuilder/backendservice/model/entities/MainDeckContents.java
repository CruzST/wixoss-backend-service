package com.wixossdeckbuilder.backendservice.model.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.wixossdeckbuilder.backendservice.model.dto.DeckAndCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Entity
@Table(name = "main_deck_contents")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs(
        {@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)}
)
public class MainDeckContents extends DeckAndCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "card_count")
    private int cardCount;

    public MainDeckContents(DeckMetaData deckMetaData, Card card, int amount) {
        super(deckMetaData, card);
        this.id = null;
        this.cardCount = amount;
    }
}
