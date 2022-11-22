package com.wixossdeckbuilder.backendservice.repository;

import com.wixossdeckbuilder.backendservice.model.entities.MainDeckContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SIGNIDeckContentsRepository extends JpaRepository<MainDeckContents, Long> {

    // Fix this query if the jpa doesnt work
    /*
    @Query(
            value = "SELECT card_serial, card_count FROM deck_contents " +
                    "WHERE deck_id = :deckId", nativeQuery = true)

     */
    List<MainDeckContents> findAllByDeckId(@Param("deckId") Long deckId);
}
