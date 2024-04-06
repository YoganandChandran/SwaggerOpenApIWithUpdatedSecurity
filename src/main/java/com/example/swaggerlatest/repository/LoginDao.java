package com.example.swaggerlatest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.swaggerlatest.entity.User;

public interface LoginDao extends JpaRepository<User, Long> {

	User findOneByUserNameIgnoreCaseAndUserPassword(String userName, String password);

	User findOneByUserNameAndUserPassword(String userName, String password);

	User findByUserName(String userName);

}
