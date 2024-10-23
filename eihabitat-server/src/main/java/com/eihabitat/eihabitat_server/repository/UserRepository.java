package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

    boolean existsByProfileName(String profileName);

    Optional<User> findByEmail(String email);

    Optional<User> findByProfileName(String profileName);

    List<User> findByProfileNameContaining(String username);

    Set<User> findAllByProfileNameContainingIgnoreCase(String profileName);
}
