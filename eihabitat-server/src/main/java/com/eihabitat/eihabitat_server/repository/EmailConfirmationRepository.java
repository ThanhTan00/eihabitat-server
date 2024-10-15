package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmationToken, String> {
    EmailConfirmationToken findByToken(String token);
    String deleteByToken(String token);
}
