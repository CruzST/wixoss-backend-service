package com.wixossdeckbuilder.backendservice.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "followed_decks")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowDeck {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "followed_deck_id")
    Deck followedDeck;

    @ManyToOne
    @JoinColumn(name = "following_user_id")
    WixossUser followingUser;
}
