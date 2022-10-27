package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.entities.Deck;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckRequest;
import com.wixossdeckbuilder.backendservice.repository.DeckRepository;
import com.wixossdeckbuilder.backendservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private UserRepository userRepository;

    public Deck createNewDeck(DeckRequest deckRequest) {
        Optional<WixossUser> user = userRepository.findById(deckRequest.getUserid());
        LocalDate todaysDate = LocalDate.now();
        LocalDate expirationDate = null;
        if (user.isEmpty()) {
            expirationDate = todaysDate.plusDays(14);
            user = null;
        }
        Boolean autoDelete = expirationDate == null ? true : false;

        Deck newDeck = new Deck(
                null,
                user.get(),
                deckRequest.getDeckName(),
                todaysDate,
                expirationDate,
                autoDelete
        );
        return deckRepository.save(newDeck);
    }

    public Deck updateDeck(Deck oldDeck, WixossUser wixossUser, String newDeckName) {
        Deck updatedDeck = new Deck(
                oldDeck.getId(),
                wixossUser,
                newDeckName,
                oldDeck.getCreationDate(),
                oldDeck.getExpirationDate(),
                oldDeck.getAutoDelete()
        );
        return updatedDeck;
    }

    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    public Optional<Deck> getSingleDeck(Long id) {
        return deckRepository.findById(id);
    }

    public void deleteDeck(Long id) {
        deckRepository.deleteById(id);
    }
}
