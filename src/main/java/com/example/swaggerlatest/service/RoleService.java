package com.example.swaggerlatest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.swaggerlatest.entity.Role;
import com.example.swaggerlatest.repository.RoleDao;

@Service
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	public Role createNewRole(Role role) {
	    return roleDao.save(role);
	}

}
