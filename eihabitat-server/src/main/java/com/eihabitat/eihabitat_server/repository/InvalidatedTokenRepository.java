package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.InvalidatedToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends CrudRepository<InvalidatedToken, String> {

}
