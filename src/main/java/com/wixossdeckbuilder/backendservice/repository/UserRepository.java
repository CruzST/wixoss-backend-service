package com.wixossdeckbuilder.backendservice.repository;

import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<WixossUser, Long> {
    WixossUser findByEmail(String email);
    WixossUser findByUsername(String username);
}
