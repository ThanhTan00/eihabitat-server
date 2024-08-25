package com.eihabitat.eihabitat_server.repository;

import com.eihabitat.eihabitat_server.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
