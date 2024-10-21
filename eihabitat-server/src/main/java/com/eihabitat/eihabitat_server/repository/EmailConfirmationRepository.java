package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmationToken, String> {
    Optional<EmailConfirmationToken> findEmailConfirmationTokenByToken(String token);

    String deleteByToken(String token);
}
