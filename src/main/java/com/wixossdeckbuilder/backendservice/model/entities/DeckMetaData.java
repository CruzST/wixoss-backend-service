package com.wixossdeckbuilder.backendservice.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class DeckMetaData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wixxossUserID")
    private WixossUser wixossUser;

    @Column(name = "deck_name") // max 255 char
    private String deckName;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "expiration_date")
    @JsonIgnore
    private LocalDate expirationDate;

    @Column(name = "auto_delete")
    @JsonIgnore
    private Boolean autoDelete;

    private int views;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate lastUpdated;
}
