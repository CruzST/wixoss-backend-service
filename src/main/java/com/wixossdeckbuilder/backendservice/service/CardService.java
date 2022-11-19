package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.entities.Card;
import com.wixossdeckbuilder.backendservice.model.payloads.CardRequest;
import com.wixossdeckbuilder.backendservice.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    public static final Logger logger = LoggerFactory.getLogger(CardService.class);

    @Autowired
    private CardRepository cardRepository;

    public Card createNewCard(CardRequest cardRequest) throws Exception {
        Optional<Card> cardInDatabase = findBySerial(cardRequest.getId());
        if (cardInDatabase.isPresent()) {
            logger.error("Card with serial: " + cardRequest.getId() + " already exists!");
            return null;
        }
        Card newCard = new Card(
                cardRequest.getId(),
                cardRequest.getName(),
                cardRequest.getRarity(),
                cardRequest.getCardType(),
                cardRequest.getLrigTypeOrClass(),
                cardRequest.getColors(),
                cardRequest.getLevel(),
                cardRequest.getGrowCost(),
                cardRequest.getCost(),
                cardRequest.getLimit(),
                cardRequest.getPower(),
                cardRequest.getTeam(),
                cardRequest.getEffects(),
                cardRequest.getLifeBurst(),
                cardRequest.getCoin(),
                cardRequest.getSetFormat(),
                cardRequest.getSerial(),
                null, //TODO: fix this with image
                cardRequest.getTiming()
        );
        return cardRepository.save(newCard);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getSingleCard(String serial) {
        return cardRepository.findById(serial);
    }

    public Card updateCard(CardRequest cardRequest) {
        Card updatedCard = new Card(
                cardRequest.getId(),
                cardRequest.getName(),
                cardRequest.getRarity(),
                cardRequest.getCardType(),
                cardRequest.getLrigTypeOrClass(),
                cardRequest.getColors(),
                cardRequest.getLevel(),
                cardRequest.getGrowCost(),
                cardRequest.getCost(),
                cardRequest.getLimit(),
                cardRequest.getPower(),
                cardRequest.getTeam(),
                cardRequest.getEffects(),
                cardRequest.getLifeBurst(),
                cardRequest.getCoin(),
                cardRequest.getSetFormat(),
                cardRequest.getSerial(),
                null, //TODO: fix this eventually
                cardRequest.getTiming()
        );
        return cardRepository.save(updatedCard);
    }

    public void deleteCard(String serial) {
        cardRepository.deleteById(serial);
    }

    public Optional<Card> findBySerial(String serial) {
        return cardRepository.findById(serial);
    }

    public List<Card> findAllByTiming() {
        return cardRepository.findAllWithTiming();
    }

    public void saveAllCards(List<Card> cards) {
        cardRepository.saveAll(cards);
    }
}
