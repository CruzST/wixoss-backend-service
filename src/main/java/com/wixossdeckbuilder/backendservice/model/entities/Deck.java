package com.wixossdeckbuilder.backendservice.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "decks")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wixxossUserID")
    private WixossUser wixossUser;

    @Column(name = "deck_name")
    private String deckName;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "auto_delete")
    private Boolean autoDelete;
}
