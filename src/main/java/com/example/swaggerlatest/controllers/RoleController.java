package com.example.swaggerlatest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.swaggerlatest.entity.Role;
import com.example.swaggerlatest.service.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("/createNewRole")
	public Role createNewRole(@RequestBody Role role) {
		return roleService.createNewRole(role);
	}

}
