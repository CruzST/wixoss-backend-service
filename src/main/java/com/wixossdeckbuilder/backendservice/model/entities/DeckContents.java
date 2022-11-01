package com.wixossdeckbuilder.backendservice.model.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

@Entity
@Table(name = "deck_contents")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs(
        {@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)}
)
public class DeckContents {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne // Many Deck Contents to One Deck
    private Deck deck;

    @ManyToOne // Many Deck Contents to one card?
    private Card card;

    @Column(name = "card_count")
    private int cardCount;

    @Column(name = "card_name")
    private String cardName;

    @Column(name = "card_serial")
    private String cardSerial;

    public DeckContents(Deck deck, Card card, int amount, String serialNumber) {
        this.id = null;
        this.deck = deck;
        this.card = card;
        this.cardCount = amount;
        this.cardSerial = serialNumber;
    }
}
