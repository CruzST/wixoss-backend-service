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

    @ManyToOne
    private Deck deck;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Card card;

    @Column(name = "card_count")
    private int cardCount;
}
