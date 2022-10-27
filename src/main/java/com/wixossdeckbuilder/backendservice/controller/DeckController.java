package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.model.entities.Deck;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckRequest;
import com.wixossdeckbuilder.backendservice.service.DeckService;
import com.wixossdeckbuilder.backendservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deck")
public class DeckController {
    @Autowired
    private DeckService deckService;

    @Autowired
    private UserService userService;

    //new deck
    @PostMapping("/new")
    ResponseEntity<Deck> createNewDeck(@RequestBody @Valid DeckRequest deckRequest) {
        Deck newDeck = deckService.createNewDeck(deckRequest);
        return ResponseEntity.ok(newDeck);
    }

    //update deck
    @PutMapping("/update/{id}")
    ResponseEntity<Deck> updateDeck(@RequestBody @Valid DeckRequest deckRequest, Long id) {
        Optional<Deck> deckToUpdate = deckService.getSingleDeck(id);
        Optional<WixossUser> deckOwner = userService.getSingleUser(
                deckToUpdate.get().getWixossUser().getId()
        );

        if (deckToUpdate.isPresent()){
            // LOGIC
            Deck oldDeck = deckToUpdate.get();
            Deck updatedDeck = deckService.updateDeck(oldDeck, deckOwner.get(), deckRequest.getDeckName());
            return ResponseEntity.ok(updatedDeck);
        }
        return ResponseEntity.notFound().build();
    }


    //get all
    @GetMapping("/all")
    ResponseEntity<List<Deck>> getAllDecks() {
        return ResponseEntity.ok(deckService.getAllDecks());
    }

    //get one
    @GetMapping("/{id}")
    ResponseEntity<Deck> getSingleDeck(@PathVariable(value = "id") Long id) {
        Optional<Deck> deck = deckService.getSingleDeck(id);
        if (deck.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deck.get());
    }

    //delete one
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteDeck(@PathVariable(value = "id") Long id) {
        Optional<Deck> deckToDelete = deckService.getSingleDeck(id);
        if (deckToDelete.isPresent()) {
            deckService.deleteDeck(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
