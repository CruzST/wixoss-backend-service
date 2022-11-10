package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.model.baseClasses.MainDeck;
import com.wixossdeckbuilder.backendservice.model.baseClasses.MainDeckContent;
import com.wixossdeckbuilder.backendservice.model.entities.Deck;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckContentsRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckContext;
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

    /**
     *
     * Deck as whole functions
     *
     * **/
    @PostMapping("/new")
    ResponseEntity<Deck> createNewDeck(@RequestBody @Valid DeckContext newDeckContext) {
        Deck newDeck = deckService.createNewDeck(newDeckContext.getDeckRequest());
        newDeckContext.getDeckContentsRequest().setDeckId(newDeck.getId());
        Deck updatedDeck = deckService.addCardsToDeck(newDeckContext.getDeckContentsRequest());
        return ResponseEntity.ok(updatedDeck);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<Deck> updateDeck(@RequestBody @Valid DeckContext updatedDeckContext,
                                    @PathVariable(value = "id") Long deckId) {
        Optional<Deck> deckToUpdate = deckService.getDeckMetaData(deckId);
        if (deckToUpdate.isPresent()){
            Deck updatedDeck = deckService.updateDeck(deckId, updatedDeckContext);
            return ResponseEntity.ok(updatedDeck);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    ResponseEntity<List<MainDeck>> getAllDecks() {
        return ResponseEntity.ok(deckService.getAllDecks());
    }

    @GetMapping("/{id}")
    ResponseEntity<MainDeck> getSingleDeck(@PathVariable(value = "id") Long id) {
        Optional<MainDeck> deck = deckService.getSingleDeck(id);
        if (deck.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deck.get());
    }

    @DeleteMapping("/delete/{id}/{ownerId}")
    ResponseEntity<?> deleteDeck(@PathVariable(value = "id") Long id, @PathVariable(value = "ownerId") Long ownerId) {
        Optional<Deck> deckToDelete = deckService.getDeckMetaData(id);
        Optional<WixossUser> deckOwner = userService.getSingleUser(ownerId);
        if (deckToDelete.isPresent() && deckOwner.isPresent() &&
                deckToDelete.get().getWixossUser().getId() == ownerId) {
            deckService.deleteDeck(id);
            return ResponseEntity.ok("Deck successfully deleted");
        }
        return ResponseEntity.notFound().build();
    }

    /** might not need these 2 end points and keep it coupled with the updating the deck as a whole**/
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
