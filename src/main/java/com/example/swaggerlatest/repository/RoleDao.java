package com.example.swaggerlatest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.swaggerlatest.entity.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, String> {

	Role findByRoleName(String string);

}
