package com.wixossdeckbuilder.backendservice.model.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FollowDeckRequest {
    Long deckId;
    Long followerUserId;
}
