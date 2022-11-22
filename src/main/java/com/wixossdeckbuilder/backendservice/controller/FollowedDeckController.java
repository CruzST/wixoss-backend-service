package com.wixossdeckbuilder.backendservice.controller;

import com.wixossdeckbuilder.backendservice.model.entities.DeckMetaData;
import com.wixossdeckbuilder.backendservice.model.entities.FollowDeck;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import com.wixossdeckbuilder.backendservice.model.payloads.FollowDeckRequest;
import com.wixossdeckbuilder.backendservice.service.DeckService;
import com.wixossdeckbuilder.backendservice.service.FollowDeckService;
import com.wixossdeckbuilder.backendservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/followDeck")
public class FollowedDeckController {


    @Autowired
    private FollowDeckService followDeckService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private UserService userService;

    @PostMapping("/follow")
    ResponseEntity<FollowDeck> createNewFollowedDeckEntry(@RequestBody @Valid FollowDeckRequest followDeckRequest) {
        Optional<DeckMetaData> deckToFollow = deckService.getDeckMetaData(followDeckRequest.getDeckId());
        Optional<WixossUser> follower = userService.getSingleUser(followDeckRequest.getFollowerUserId());
        ResponseEntity response = null;
        if (deckToFollow.isEmpty() || follower.isEmpty()) {
            response = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("ERROR: Deck or user not found!");
        } else {
            FollowDeck newFollow = followDeckService.followDeck(deckToFollow.get(), follower.get());
            response = ResponseEntity.ok(newFollow);
        }
        return response;
    }

    @DeleteMapping("/unfollow")
    ResponseEntity<?> unfollowDeck(@RequestBody @Valid FollowDeckRequest followDeckRequest) {
        Optional<FollowDeck> deckToUnfollow = followDeckService.getFollowDeck(followDeckRequest.getDeckId());
        Optional<WixossUser> follower = userService.getSingleUser(followDeckRequest.getFollowerUserId());
        if (deckToUnfollow.isEmpty() ||
                deckToUnfollow.get().getFollowingUser().getId() != followDeckRequest.getFollowerUserId()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("ERROR: Deck does not exist or does not belong to that user!");
        }
        followDeckService.unfollowDeck(deckToUnfollow.get().getId());
        return ResponseEntity
                .ok("Deck successfully unfollowed!");
    }
}
