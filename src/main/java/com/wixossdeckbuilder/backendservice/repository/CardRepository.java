package com.wixossdeckbuilder.backendservice.repository;

import com.wixossdeckbuilder.backendservice.model.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    // TODO: make query to find card by the serial
}
