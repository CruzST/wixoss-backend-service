package com.wixossdeckbuilder.backendservice.repository;

import com.wixossdeckbuilder.backendservice.model.entities.LRIGDeckContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LRIGDeckContentsRepository extends JpaRepository<LRIGDeckContents, Long> {

    List<LRIGDeckContents> findAllByDeckId(@Param("deckId") Long deckId);
}
