package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.entities.Card;
import com.wixossdeckbuilder.backendservice.model.payloads.CardRequest;
import com.wixossdeckbuilder.backendservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    public Card createNewCard(CardRequest cardRequest) {
        Card newCard = new Card(
                null,
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
                cardRequest.getSerial()
        );
        return cardRepository.save(newCard);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getSingleCard(Long id) {
        return cardRepository.findById(id);
    }

    public Card updateCard(CardRequest cardRequest, Long id) {
        Card updatedCard = new Card(
                id,
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
                cardRequest.getSerial()
        );
        return cardRepository.save(updatedCard);
    }

    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    public void findBySerial(String serial) {
        // TODO: Update function
    }
}
