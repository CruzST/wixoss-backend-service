package com.wixossdeckbuilder.backendservice.repository;

import com.wixossdeckbuilder.backendservice.model.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    // TODO: make query to find card by the serial
    @Query(
            value = "SELECT * FROM cards c " +
            "WHERE c.serial->>'serialNumber' = :serialToFind", nativeQuery = true)
    Optional<Card> findBySerial(@Param("serialToFind") String serialToFind);
}
