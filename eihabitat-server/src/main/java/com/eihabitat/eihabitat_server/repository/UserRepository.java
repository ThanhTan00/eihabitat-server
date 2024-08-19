package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
