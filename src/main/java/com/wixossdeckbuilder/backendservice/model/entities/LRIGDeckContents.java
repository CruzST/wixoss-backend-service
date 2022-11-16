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
@Table(name="lrig_deck_contents")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TypeDefs(
        {@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)}
)
public class LRIGDeckContents extends DeckAndCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public LRIGDeckContents(Deck deck, Card card) {
        super(deck, card);
    }
}
