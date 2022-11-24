package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.dto.DeckCardsMetaData;
import com.wixossdeckbuilder.backendservice.model.dto.Deck;
import com.wixossdeckbuilder.backendservice.model.dto.DeckContent;
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
    public DeckMetaData createNewDeck(DeckRequest deckRequest) {
        WixossUser owner = null;
        LocalDate todaysDate = LocalDate.now();
        LocalDate expirationDate = null;
        if (deckRequest.getDeckOwnerId() != null) {
            owner = userRepository.findById(deckRequest.getDeckOwnerId()).get();
        } else {
            expirationDate = todaysDate.plusDays(14);
        }
        Boolean autoDelete = expirationDate == null ? false : true;
        DeckMetaData newDeckMetaData = new DeckMetaData(
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
        return deckRepository.save(newDeckMetaData);
    }

    public List<Deck> getAllDecks() {
        List<Deck> decks = new ArrayList<>();
        List<DeckMetaData> decksToConvert = deckRepository.findAll();
        decksToConvert.forEach(deck -> {
            List<MainDeckContents> mainDeckContents = signiDeckContentsRepository.findAllByDeckId(deck.getId());
            List<LRIGDeckContents> lrigDeckContent = lrigDeckContentsRepository.findAllByDeckId(deck.getId());
            Deck mainDeck = convertToDeck(deck.getId(), deck.getDeckName(), mainDeckContents, lrigDeckContent);
            decks.add(mainDeck);
        });
        return decks;
    }

    // a new object that has the card objects, the function that converts it from the deck content to main deck might be its own function
    public Optional<Deck> getSingleDeck(Long id) {
        DeckMetaData deckMetaData = deckRepository.getReferenceById(id);
        List<MainDeckContents> mainDeckContents = signiDeckContentsRepository.findAllByDeckId(id);
        List<LRIGDeckContents> lrigDeckContent = lrigDeckContentsRepository.findAllByDeckId(id);
        Deck deck = convertToDeck(id, deckMetaData.getDeckName(), mainDeckContents, lrigDeckContent);
        return Optional.ofNullable(deck);
    }

    public Optional<DeckMetaData> getDeckMetaData(Long id) {
        return deckRepository.findById(id);
    }

    public void deleteDeck(Long id) {
        // Delete card contents from signi deck
        List<MainDeckContents> signiDeckToDelete = getSigniDeckByDeckId(id);
        signiDeckContentsRepository.deleteAll(signiDeckToDelete);
        // delete card contents from lrig deck
        List<LRIGDeckContents> lrigDeckToDelete = getLrigDeckById(id);
        lrigDeckContentsRepository.deleteAll(lrigDeckToDelete);
        // set any FK in the followed table to null
        followDeckService.updateFollowedDeckToNull(id);
        deckRepository.deleteById(id);
    }

    public DeckMetaData updateDeck(Long deckId, DeckPayload updatedDeckPayload) {
        DeckMetaData oldDeckMetaData = deckRepository.findById(deckId).get();
        updateDeckMetaData(oldDeckMetaData, updatedDeckPayload.getDeckRequest());
        return editCardsInDeck(updatedDeckPayload.getDeckContentsRequest());
    }

    private void updateDeckMetaData(DeckMetaData deckMetaData, DeckRequest deckRequest) {
        DeckMetaData updatedDeckMetaData = new DeckMetaData(
                deckMetaData.getId(),
                deckMetaData.getWixossUser(),
                deckRequest.getDeckName(),
                deckMetaData.getCreationDate(),
                deckMetaData.getExpirationDate(),
                deckMetaData.getAutoDelete(),
                deckMetaData.getViews(),
                deckRequest.getDescription(),
                LocalDate.now()
        );
        deckRepository.save(updatedDeckMetaData);
    }

    private DeckMetaData updateDeckLastUpdatedTimeStamp(DeckMetaData deckMetaData) {
        deckMetaData.setLastUpdated(LocalDate.now());
        return deckRepository.save(deckMetaData);
    }

    public DeckMetaData addCardsToDeck(DeckContentsRequest deckContents) {
        DeckMetaData deckMetaData = deckRepository.getReferenceById(deckContents.getDeckId());

        List<MainDeckContents> signiDeckArr = createSigniList(deckMetaData, deckContents.getMainDeck());
        signiDeckContentsRepository.saveAll(signiDeckArr);

        List<LRIGDeckContents> lrigDeckArr = createLrigList(deckMetaData, deckContents.getLrigDeck());
        lrigDeckContentsRepository.saveAll(lrigDeckArr);

        return updateDeckLastUpdatedTimeStamp(deckMetaData);
    }

    public DeckMetaData editCardsInDeck(DeckContentsRequest updatedDeckContentsRequest) {
        DeckMetaData deckMetaData = deckRepository.getReferenceById(updatedDeckContentsRequest.getDeckId());
        editCardsInSigniDeck(deckMetaData, updatedDeckContentsRequest.getMainDeck());
        editCardsInLrigDeck(deckMetaData, updatedDeckContentsRequest.getLrigDeck());
        return updateDeckLastUpdatedTimeStamp(deckMetaData);
    }

    public List<MainDeckContents> getSigniDeckByDeckId(Long id) {
        return signiDeckContentsRepository.findAllByDeckId(id);
    }

    public List<LRIGDeckContents> getLrigDeckById(Long id) {
        return lrigDeckContentsRepository.findAllByDeckId(id);
    }

    /** Helper functions **/
    private void editCardsInSigniDeck(DeckMetaData deckMetaData, List<DeckCardsMetaData> updatedSigniDeck) {
        // Create a new list of deck contents SIGNI, Get the current deck contents as stored in the database SIGNI
        List<MainDeckContents> updatedSigniContents = createSigniList(deckMetaData, updatedSigniDeck);
        List<MainDeckContents> originalSigniContents = signiDeckContentsRepository.findAllByDeckId(deckMetaData.getId());

        //Remove objects each list to determine what to remove/add
        List<MainDeckContents> MainDeckContentsToBeRemoved = removeFromSigniList(originalSigniContents, updatedSigniContents);
        List<MainDeckContents> MainDeckContentsToAddToDB = removeFromSigniList(updatedSigniContents, originalSigniContents);

        signiDeckContentsRepository.deleteAll(MainDeckContentsToBeRemoved);
        signiDeckContentsRepository.saveAll(MainDeckContentsToAddToDB);
    }

    private void editCardsInLrigDeck(DeckMetaData deckMetaData, List<String> updatedLrigDeck) {
        // Create a new list of deck contents LRIG
        List<LRIGDeckContents> updatedLrigContents = createLrigList(deckMetaData, updatedLrigDeck);
        List<LRIGDeckContents> originalLrigContents = lrigDeckContentsRepository.findAllByDeckId(deckMetaData.getId());

        List<LRIGDeckContents> contentsToBeRemoved = removeFromLrigList(originalLrigContents, updatedLrigContents);
        List<LRIGDeckContents> deckContentsToAddToDB = removeFromLrigList(updatedLrigContents, originalLrigContents);

        lrigDeckContentsRepository.deleteAll(contentsToBeRemoved);
        lrigDeckContentsRepository.saveAll(deckContentsToAddToDB);
    }

    private List<MainDeckContents> createSigniList(DeckMetaData deckMetaData, List<DeckCardsMetaData> deckContents) {
        ArrayList<MainDeckContents> deckContentArr = new ArrayList<>();
        for (DeckCardsMetaData cardInfo : deckContents) {
            Card cardToFind = cardService.findBySerial(cardInfo.getCardSerial()).get();
            MainDeckContents MainDeckContentsToAdd =  new MainDeckContents(
                    deckMetaData,
                    cardToFind,
                    cardInfo.getAmount()
            );
            deckContentArr.add(MainDeckContentsToAdd);
        }
        return deckContentArr;
    }

    private List<LRIGDeckContents> createLrigList(DeckMetaData deckMetaData, List<String> deckContents) {
        ArrayList<LRIGDeckContents> deckContentArr = new ArrayList<>();
        for (String cardInfo : deckContents) {
            Card cardToFind = cardService.findBySerial(cardInfo).get();
            LRIGDeckContents LRIGDeckContentsToAdd =  new LRIGDeckContents(
                    deckMetaData,
                    cardToFind
            );
            deckContentArr.add(LRIGDeckContentsToAdd);
        }
        return deckContentArr;
    }

    private Deck convertToDeck(Long id, String deckName, List<MainDeckContents> mainDeckContents,
                               List<LRIGDeckContents> lrigDeckContents) {
        List<DeckContent> mainDeck = new ArrayList<>();
        mainDeckContents.forEach(signiCard -> {
            DeckContent card = new DeckContent(signiCard.getCard(), signiCard.getCardCount());
            mainDeck.add(card);
        });

        List<DeckContent> lrigDeck = new ArrayList<>();
        lrigDeckContents.forEach(lrig -> {
            DeckContent card = new DeckContent(lrig.getCard(), 1);
            lrigDeck.add(card);
        });
        return new Deck(id, deckName, mainDeck, lrigDeck);
    }

    // Returns objects from the original list that are not equal objects in the check list
    private List<MainDeckContents> removeFromSigniList(List<MainDeckContents> originalList, List<MainDeckContents> checkList) {
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
