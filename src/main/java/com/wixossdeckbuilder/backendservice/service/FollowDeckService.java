package com.wixossdeckbuilder.backendservice.service;

import com.wixossdeckbuilder.backendservice.model.entities.DeckMetaData;
import com.wixossdeckbuilder.backendservice.model.entities.FollowDeck;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.repository.FollowDeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FollowDeckService {

    @Autowired
    FollowDeckRepository followDeckRepository;

    public FollowDeck followDeck(DeckMetaData deckMetaDataToFollow, WixossUser follower) {
        FollowDeck newFollow = new FollowDeck(null, deckMetaDataToFollow, follower);
        return followDeckRepository.save(newFollow);
    }

    public void unfollowDeck(Long followedDeckId) {
        followDeckRepository.deleteById(followedDeckId);
    }

    public Optional<FollowDeck> getFollowDeck(Long id) {
        return followDeckRepository.findById(id);
    }

    public void updateFollowedDeckToNull(Long deckToNullId) {
        followDeckRepository.updateFollowedDecksToNull(deckToNullId);
    }

}
