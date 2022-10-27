package com.wixossdeckbuilder.backendservice.repository;

import com.wixossdeckbuilder.backendservice.model.entities.DeckContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeckContentsRepository extends JpaRepository<DeckContents, Long> {
}
