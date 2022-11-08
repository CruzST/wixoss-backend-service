package com.wixossdeckbuilder.backendservice.repository;

import com.wixossdeckbuilder.backendservice.model.entities.FollowDeck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FollowDeckRepository extends JpaRepository<FollowDeck, Long> {
    @Modifying
    @Transactional
    @Query(value =
            "UPDATE followed_decks " +
            "SET followed_deck_id = null " +
            "WHERE followed_deck_id = :deletedDeckId", nativeQuery = true)
    void updateFollowedDecksToNull(@Param("deletedDeckId") Long id);
}
