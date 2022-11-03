package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.model.entities.Deck;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckContentsRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckRequest;
import com.wixossdeckbuilder.backendservice.service.DeckService;
import com.wixossdeckbuilder.backendservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deck")
public class DeckController {
    @Autowired
    private DeckService deckService;

    @Autowired
    private UserService userService;

    @PostMapping("/new")
    ResponseEntity<Deck> createNewDeck(@RequestBody @Valid DeckRequest deckRequest,
                                       @RequestBody @Valid DeckContentsRequest deckContentsRequest) {
        Deck newDeck = deckService.createNewDeck(deckRequest);
        deckContentsRequest.setDeckId(newDeck.getId());
        Deck updatedDeck = deckService.addCardsToDeck(deckContentsRequest);
        return ResponseEntity.ok(updatedDeck);
    }

    // TODO: Flesh this out to be more logical
    @PutMapping("/update/{id}")
    ResponseEntity<Deck> updateDeck(@RequestBody @Valid DeckRequest deckRequest,
                                    @RequestBody @Valid Long id) {
        Optional<Deck> deckToUpdate = deckService.getSingleDeck(id);
        WixossUser deckOwner = userService.getReferenceToUserById(deckToUpdate.get().getWixossUser().getId());

        if (deckToUpdate.isPresent()){
            // LOGIC
            Deck oldDeck = deckToUpdate.get();
            Deck updatedDeck = deckService.updateDeck(oldDeck, deckOwner, deckRequest);
            return ResponseEntity.ok(updatedDeck);
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/all")
    ResponseEntity<List<Deck>> getAllDecks() {
        return ResponseEntity.ok(deckService.getAllDecks());
    }

    // TODO: Flesh this out
    @GetMapping("/{id}")
    ResponseEntity<Deck> getSingleDeck(@PathVariable(value = "id") Long id) {
        Optional<Deck> deck = deckService.getSingleDeck(id);
        if (deck.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deck.get());
    }

    // TODO: Flesh this out
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteDeck(@PathVariable(value = "id") Long id) {
        Optional<Deck> deckToDelete = deckService.getSingleDeck(id);
        if (deckToDelete.isPresent()) {
            deckService.deleteDeck(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/addDeckCards")
    ResponseEntity<Deck> addDeckContents(@RequestBody @Valid DeckContentsRequest deckContentsRequest) {
        Deck updatedDeck = deckService.addCardsToDeck(deckContentsRequest);
        return ResponseEntity.ok().body(updatedDeck);
    }

    @PutMapping("/updateDeckCards")
    ResponseEntity<Deck> updateDeckContents(@RequestBody @Valid DeckContentsRequest deckContentsRequest) {
        Deck updatedDeck = deckService.editCardsInDeck(deckContentsRequest);
        return ResponseEntity.ok().body(updatedDeck);
    }
}
