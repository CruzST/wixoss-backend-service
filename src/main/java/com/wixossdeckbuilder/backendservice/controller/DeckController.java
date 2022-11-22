package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.model.dto.Deck;
import com.wixossdeckbuilder.backendservice.model.entities.DeckMetaData;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckContentsRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckPayload;
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
    ResponseEntity<DeckMetaData> createNewDeck(@RequestBody @Valid DeckPayload newDeckPayload) {
        DeckMetaData newDeckMetaData = deckService.createNewDeck(newDeckPayload.getDeckRequest());
        newDeckPayload.getDeckContentsRequest().setDeckId(newDeckMetaData.getId());
        DeckMetaData updatedDeckMetaData = deckService.addCardsToDeck(newDeckPayload.getDeckContentsRequest());
        return ResponseEntity.ok(updatedDeckMetaData);
    }

    @PutMapping("/update/{id}")
    ResponseEntity<DeckMetaData> updateDeck(@RequestBody @Valid DeckPayload updatedDeckPayload,
                                            @PathVariable(value = "id") Long deckId) {
        Optional<DeckMetaData> deckToUpdate = deckService.getDeckMetaData(deckId);
        if (deckToUpdate.isPresent()){
            DeckMetaData updatedDeckMetaData = deckService.updateDeck(deckId, updatedDeckPayload);
            return ResponseEntity.ok(updatedDeckMetaData);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all")
    ResponseEntity<List<Deck>> getAllDecks() {
        return ResponseEntity.ok(deckService.getAllDecks());
    }

    @GetMapping("/{id}")
    ResponseEntity<Deck> getSingleDeck(@PathVariable(value = "id") Long id) {
        Optional<Deck> deck = deckService.getSingleDeck(id);
        if (deck.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deck.get());
    }

    @DeleteMapping("/delete/{id}/{ownerId}")
    ResponseEntity<?> deleteDeck(@PathVariable(value = "id") Long id, @PathVariable(value = "ownerId") Long ownerId) {
        Optional<DeckMetaData> deckToDelete = deckService.getDeckMetaData(id);
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
    ResponseEntity<DeckMetaData> addDeckContents(@RequestBody @Valid DeckContentsRequest deckContentsRequest) {
        DeckMetaData updatedDeckMetaData = deckService.addCardsToDeck(deckContentsRequest);
        return ResponseEntity.ok().body(updatedDeckMetaData);
    }

    @PutMapping("/updateDeckCards")
    ResponseEntity<DeckMetaData> updateDeckContents(@RequestBody @Valid DeckContentsRequest deckContentsRequest) {
        DeckMetaData updatedDeckMetaData = deckService.editCardsInDeck(deckContentsRequest);
        return ResponseEntity.ok().body(updatedDeckMetaData);
    }
}
