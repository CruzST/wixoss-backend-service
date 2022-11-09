package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.baseClasses.DeckCards;
import com.wixossdeckbuilder.backendservice.model.entities.*;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckContentsRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckContext;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckRequest;
import com.wixossdeckbuilder.backendservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private SIGNIDeckContentsRepository signiDeckContentsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LRIGDeckContentsRepository lrigDeckContentsRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private FollowDeckService followDeckService;


    /**
     *
     * Deck as whole functions
     *
     * **/
    public Deck createNewDeck(DeckRequest deckRequest) {
        Optional<WixossUser> user = userRepository.findById(deckRequest.getDeckOwnerId());
        LocalDate todaysDate = LocalDate.now();
        LocalDate expirationDate = null;
        if (user.isEmpty()) {
            expirationDate = todaysDate.plusDays(14);
            user = null;
        }
        Boolean autoDelete = expirationDate == null ? false : true;

        Deck newDeck = new Deck(
                null,
                user.get(),
                deckRequest.getDeckName(),
                todaysDate,
                expirationDate,
                autoDelete,
                0,
                deckRequest.getDescription(),
                todaysDate
        );
        return deckRepository.save(newDeck);
    }

    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }

    // TODO: Flesh out to return deck data from signi and lrig too should probably return
    // a new object that has the card object
    public Optional<Deck> getSingleDeck(Long id) {
        return deckRepository.findById(id);
    }

    public void deleteDeck(Long id) {
        // Delete card contents from signi deck
        List<SIGNIDeckContents> signiDeckToDelete = getSigniDeckByDeckId(id);
        signiDeckContentsRepository.deleteAll(signiDeckToDelete);
        // delete card contents from lrig deck
        List<LRIGDeckContents> lrigDeckToDelete = getLrigDeckById(id);
        lrigDeckContentsRepository.deleteAll(lrigDeckToDelete);
        // set any FK in the followed table to null
        followDeckService.updateFollowedDeckToNull(id);
        deckRepository.deleteById(id);
    }

    public Deck updateDeck(Long deckId, DeckContext updatedDeckContext) {
        Deck oldDeck = deckRepository.findById(deckId).get();
        updateDeckMetaData(oldDeck, updatedDeckContext.getDeckRequest());
        return editCardsInDeck(updatedDeckContext.getDeckContentsRequest());
    }

    private void updateDeckMetaData(Deck deck, DeckRequest deckRequest) {
        Deck updatedDeck = new Deck(
                deck.getId(),
                deck.getWixossUser(),
                deckRequest.getDeckName(),
                deck.getCreationDate(),
                deck.getExpirationDate(),
                deck.getAutoDelete(),
                deck.getViews(),
                deckRequest.getDescription(),
                LocalDate.now()
        );
        deckRepository.save(updatedDeck);
    }

    private Deck updateDeckLastUpdatedTimeStamp(Deck deck) {
        deck.setLastUpdated(LocalDate.now());
        return deckRepository.save(deck);
    }

    public Deck addCardsToDeck(DeckContentsRequest deckContents) {
        Deck deck = deckRepository.getReferenceById(deckContents.getDeckId());

        List<SIGNIDeckContents> signiDeckArr = createSigniList(deck, deckContents.getSigniDeck());
        signiDeckContentsRepository.saveAll(signiDeckArr);

        List<LRIGDeckContents> lrigDeckArr = createLrigList(deck, deckContents.getLrigDeck());
        lrigDeckContentsRepository.saveAll(lrigDeckArr);

        return updateDeckLastUpdatedTimeStamp(deck);
    }

    public Deck editCardsInDeck(DeckContentsRequest updatedDeckContentsRequest) {
        Deck deck = deckRepository.getReferenceById(updatedDeckContentsRequest.getDeckId());
        editCardsInSigniDeck(deck, updatedDeckContentsRequest.getSigniDeck());
        editCardsInLrigDeck(deck, updatedDeckContentsRequest.getLrigDeck());
        return updateDeckLastUpdatedTimeStamp(deck);
    }

    /**
     *
     * Functions that relate to the SIGNI Deck
     *
     * **/
    private void editCardsInSigniDeck(Deck deck, List<DeckCards> updatedSigniDeck) {
        // Create a new list of deck contents SIGNI
        List<SIGNIDeckContents> updatedSIGNIDeckContents = createSigniList(deck, updatedSigniDeck);

        // Get the current deck contents as stored in the database SIGNI
        List<SIGNIDeckContents> SIGNIDeckContentsInDB = signiDeckContentsRepository.findAllByDeckId(deck.getId());
        List<SIGNIDeckContents> SIGNIDeckContentsToBeRemoved = new ArrayList<>(SIGNIDeckContentsInDB);

        //Remove objects from deckContentsToBeRemoved that are in updatedDeckContents SIGNI
        SIGNIDeckContentsToBeRemoved.removeAll(updatedSIGNIDeckContents);
        signiDeckContentsRepository.deleteAll(SIGNIDeckContentsToBeRemoved);

        List<SIGNIDeckContents> SIGNIDeckContentsToAddToDB = new ArrayList<>(updatedSIGNIDeckContents);
        SIGNIDeckContentsToAddToDB.removeAll(SIGNIDeckContentsInDB);
        signiDeckContentsRepository.saveAll(SIGNIDeckContentsToAddToDB);
    }

    private List<SIGNIDeckContents> createSigniList(Deck deck, List<DeckCards> deckContents) {
        ArrayList<SIGNIDeckContents> deckContentArr = new ArrayList<>();
        for (DeckCards cardInfo : deckContents) {
            Card cardToFind = cardService.findBySerial(cardInfo.getCardSerial()).get();
            SIGNIDeckContents SIGNIDeckContentsToAdd =  new SIGNIDeckContents(
                    deck,
                    cardToFind,
                    cardInfo.getAmount()
            );
            deckContentArr.add(SIGNIDeckContentsToAdd);
        }
        return deckContentArr;
    }

    public List<SIGNIDeckContents> getSigniDeckByDeckId(Long id) {
        return signiDeckContentsRepository.findAllByDeckId(id);
    }

    /**
     *
     * Functions that relate to the LRIG Deck
     *
     * **/

    private void editCardsInLrigDeck(Deck deck, List<String> updatedLrigDeck) {
        // Create a new list of deck contents LRIG
        List<LRIGDeckContents> updatedLRIGDeckContents = createLrigList(deck, updatedLrigDeck);

        // Get the current deck contents as stored in the database LRIG
        List<LRIGDeckContents> LRIGDeckContentsInDB = lrigDeckContentsRepository.findAllByDeckId(deck.getId());
        List<LRIGDeckContents> contentsToBeRemoved = new ArrayList<>(LRIGDeckContentsInDB);

        // Remove objects from deckContentsToBeRemoved that are in updatedDeckContents LRIG
        contentsToBeRemoved.removeAll(updatedLRIGDeckContents);
        lrigDeckContentsRepository.deleteAll(contentsToBeRemoved);

        List<LRIGDeckContents> deckContentsToAddToDB = new ArrayList<>(updatedLRIGDeckContents);
        deckContentsToAddToDB.removeAll(LRIGDeckContentsInDB);
        lrigDeckContentsRepository.saveAll(deckContentsToAddToDB);
    }

    private List<LRIGDeckContents> createLrigList(Deck deck, List<String> deckContents) {
        ArrayList<LRIGDeckContents> deckContentArr = new ArrayList<>();
        for (String cardInfo : deckContents) {
            Card cardToFind = cardService.findBySerial(cardInfo).get();
            LRIGDeckContents LRIGDeckContentsToAdd =  new LRIGDeckContents(
                    deck,
                    cardToFind
            );
            deckContentArr.add(LRIGDeckContentsToAdd);
        }
        return deckContentArr;
    }

    public List<LRIGDeckContents> getLrigDeckById(Long id) {
        return lrigDeckContentsRepository.findAllByDeckId(id);
    }
}
