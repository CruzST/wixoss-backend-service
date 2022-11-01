package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.config.security.jwt.JWTTokenProvider;
import com.wixossdeckbuilder.backendservice.model.entities.Card;
import com.wixossdeckbuilder.backendservice.model.payloads.CardRequest;
import com.wixossdeckbuilder.backendservice.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/card")
public class CardController {
    @Autowired
    private CardService cardService;

    public static final Logger logger = LoggerFactory.getLogger(CardService.class);

    //create card
    @PostMapping("/new")
    ResponseEntity<Card> createNewCard(@RequestBody @Valid CardRequest cardRequest) {
        ResponseEntity response = null;
        Card savedCard = null;
        try {
            savedCard = cardService.createNewCard(cardRequest);
            if (savedCard.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(savedCard);
            } else {
                logger.error("Card was not saved!");
            }
        } catch (Exception e){
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occurred during card creation: " + e.getMessage());
        }
        return response;
    }

    //get all cards
    @GetMapping("/all")
    ResponseEntity<List<Card>> getAllCards() {
        return  ResponseEntity.ok(cardService.getAllCards());
    }

    //get single card
    @GetMapping("/{id}")
    ResponseEntity<Card> getSingleCard(@PathVariable(value = "id") Long id) {
        Optional<Card> card = cardService.getSingleCard(id);

        /* Method 1
        return card.map(resp -> ResponseEntity.ok().body(resp))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
         */
        /* Method 2 */
        if (card.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card.get());
    }

    //update card
    @PutMapping("/update/{id}")
    ResponseEntity<Card> updateCard(@RequestBody @Valid CardRequest cardRequest, Long id) {
        Optional<Card> cardToUpdate = cardService.getSingleCard(id);
        if (cardToUpdate.isPresent()) {
            Card result = cardService.updateCard(cardRequest, id);
            return ResponseEntity.ok().body(result);
        }
        return ResponseEntity.notFound().build();
    }

    //delete card
    @DeleteMapping("/delete/{id}")
    ResponseEntity<?> deleteCard(@PathVariable(value = "id") Long id) {
        Optional<Card> cardToDelete = cardService.getSingleCard(id);
        if (cardToDelete.isPresent()) {
            cardService.deleteCard(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/serial/{serial}")
    ResponseEntity<Card> getCardBySerial(@PathVariable(value = "serial") String serial) {
        Optional<Card> card = cardService.findBySerial(serial);
        if (card.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(card.get());
    }
}
