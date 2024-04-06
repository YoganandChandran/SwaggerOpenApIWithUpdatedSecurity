package com.example.swaggerlatest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.swaggerlatest.entity.User;

@Repository
public interface UserDao extends CrudRepository<User, Long>{

	User findByUserName(String userName);
	Boolean existsByUserName(String userName);
	
}
