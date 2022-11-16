package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.dto.DeckCards;
import com.wixossdeckbuilder.backendservice.model.dto.MainDeck;
import com.wixossdeckbuilder.backendservice.model.dto.MainDeckContent;
import com.wixossdeckbuilder.backendservice.model.entities.*;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckContentsRequest;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckPayload;
import com.wixossdeckbuilder.backendservice.model.payloads.DeckRequest;
import com.wixossdeckbuilder.backendservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    /** Deck as whole functions **/
    public Deck createNewDeck(DeckRequest deckRequest) {
        WixossUser owner = null;
        LocalDate todaysDate = LocalDate.now();
        LocalDate expirationDate = null;
        if (deckRequest.getDeckOwnerId() != null) {
            owner = userRepository.findById(deckRequest.getDeckOwnerId()).get();
        } else {
            expirationDate = todaysDate.plusDays(14);
        }
        Boolean autoDelete = expirationDate == null ? false : true;
        Deck newDeck = new Deck(
                null,
                owner,
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

    public List<MainDeck> getAllDecks() {
        List<MainDeck> mainDecks = new ArrayList<>();
        List<Deck> decksToConvert = deckRepository.findAll();
        decksToConvert.forEach(deck -> {
            List<SIGNIDeckContents> signiDeckContent = signiDeckContentsRepository.findAllByDeckId(deck.getId());
            List<LRIGDeckContents> lrigDeckContent = lrigDeckContentsRepository.findAllByDeckId(deck.getId());
            MainDeck mainDeck = convertToMainDeck(deck.getId(), deck.getDeckName(), signiDeckContent, lrigDeckContent);
            mainDecks.add(mainDeck);
        });
        return mainDecks;
    }

    // a new object that has the card objects, the function that converts it from the deck content to main deck might be its own function
    public Optional<MainDeck> getSingleDeck(Long id) {
        Deck deckMetaData = deckRepository.getReferenceById(id);
        List<SIGNIDeckContents> signiDeckContent = signiDeckContentsRepository.findAllByDeckId(id);
        List<LRIGDeckContents> lrigDeckContent = lrigDeckContentsRepository.findAllByDeckId(id);
        MainDeck mainDeck = convertToMainDeck(id, deckMetaData.getDeckName(), signiDeckContent, lrigDeckContent);
        return Optional.ofNullable(mainDeck);
    }

    public Optional<Deck> getDeckMetaData(Long id) {
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

    public Deck updateDeck(Long deckId, DeckPayload updatedDeckPayload) {
        Deck oldDeck = deckRepository.findById(deckId).get();
        updateDeckMetaData(oldDeck, updatedDeckPayload.getDeckRequest());
        return editCardsInDeck(updatedDeckPayload.getDeckContentsRequest());
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

    public List<SIGNIDeckContents> getSigniDeckByDeckId(Long id) {
        return signiDeckContentsRepository.findAllByDeckId(id);
    }

    public List<LRIGDeckContents> getLrigDeckById(Long id) {
        return lrigDeckContentsRepository.findAllByDeckId(id);
    }

    /** Helper functions **/
    private void editCardsInSigniDeck(Deck deck, List<DeckCards> updatedSigniDeck) {
        // Create a new list of deck contents SIGNI, Get the current deck contents as stored in the database SIGNI
        List<SIGNIDeckContents> updatedSigniContents = createSigniList(deck, updatedSigniDeck);
        List<SIGNIDeckContents> originalSigniContents = signiDeckContentsRepository.findAllByDeckId(deck.getId());

        //Remove objects each list to determine what to remove/add
        List<SIGNIDeckContents> SIGNIDeckContentsToBeRemoved = removeFromSigniList(originalSigniContents, updatedSigniContents);
        List<SIGNIDeckContents> SIGNIDeckContentsToAddToDB = removeFromSigniList(updatedSigniContents, originalSigniContents);

        signiDeckContentsRepository.deleteAll(SIGNIDeckContentsToBeRemoved);
        signiDeckContentsRepository.saveAll(SIGNIDeckContentsToAddToDB);
    }

    private void editCardsInLrigDeck(Deck deck, List<String> updatedLrigDeck) {
        // Create a new list of deck contents LRIG
        List<LRIGDeckContents> updatedLrigContents = createLrigList(deck, updatedLrigDeck);
        List<LRIGDeckContents> originalLrigContents = lrigDeckContentsRepository.findAllByDeckId(deck.getId());

        List<LRIGDeckContents> contentsToBeRemoved = removeFromLrigList(originalLrigContents, updatedLrigContents);
        List<LRIGDeckContents> deckContentsToAddToDB = removeFromLrigList(updatedLrigContents, originalLrigContents);

        lrigDeckContentsRepository.deleteAll(contentsToBeRemoved);
        lrigDeckContentsRepository.saveAll(deckContentsToAddToDB);
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

    private MainDeck convertToMainDeck(Long id, String deckName, List<SIGNIDeckContents> signiDeckContents,
                                       List<LRIGDeckContents> lrigDeckContents) {
        List<MainDeckContent> signiDeck = new ArrayList<>();
        signiDeckContents.forEach(signiCard -> {
            MainDeckContent card = new MainDeckContent(signiCard.getCard(), signiCard.getCardCount());
            signiDeck.add(card);
        });

        List<MainDeckContent> lrigDeck = new ArrayList<>();
        lrigDeckContents.forEach(lrig -> {
            MainDeckContent card = new MainDeckContent(lrig.getCard(), 1);
            lrigDeck.add(card);
        });
        return new MainDeck(id, deckName, signiDeck, lrigDeck);
    }

    // Returns objects from the original list that are not equal objects in the check list
    private List<SIGNIDeckContents> removeFromSigniList(List<SIGNIDeckContents> originalList, List<SIGNIDeckContents> checkList) {
        return originalList.stream().filter(
                updatedContent -> checkList.stream().noneMatch(
                        contentInDB -> contentInDB.getCard().getSerial().equals(updatedContent.getCard().getSerial()) &&
                                contentInDB.getCardCount() == updatedContent.getCardCount()
                )
        ).collect(Collectors.toList());
    }

    private List<LRIGDeckContents> removeFromLrigList(List<LRIGDeckContents> originalList, List<LRIGDeckContents> checkList) {
        return originalList.stream().filter(
                updatedContent -> checkList.stream().noneMatch(
                        contentInDB -> contentInDB.getCard().getSerial().equals(updatedContent.getCard().getSerial())
                )
        ).collect(Collectors.toList());
    }
}
