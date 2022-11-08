package com.wixossdeckbuilder.backendservice.controller;

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
            if (savedCard != null && savedCard.getId() != null) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(savedCard);
            } else {
                logger.error("Card was not saved!");
                throw new Exception("Attempted to save card, but it was null!");
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
    @GetMapping("/{serial}")
    ResponseEntity<Card> getSingleCard(@PathVariable(value = "serial") String serial) {
        Optional<Card> card = cardService.getSingleCard(serial);

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
}
