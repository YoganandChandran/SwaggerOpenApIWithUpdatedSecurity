package com.example.swaggerlatest.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.swaggerlatest.entity.APIResponse;
import com.example.swaggerlatest.entity.LoginRequestDto;
import com.example.swaggerlatest.entity.User;
import com.example.swaggerlatest.entity.UserRequestdto;
import com.example.swaggerlatest.service.EmployeeService;
import com.example.swaggerlatest.service.JwtService;
import com.example.swaggerlatest.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;


@RestController
@Validated
@SecurityRequirement(name = "Bearer Authentication")
public class EmployeeController {


	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/employee/{id}")
	@PreAuthorize("hasRole('Role_User')")
	//@SecurityRequirement(name = "Bearer Authentication")
	public Optional<User> getEmployeeById(@PathVariable("id") Integer id){
		return employeeService.getEmployeeById(id);
	}
	
	@GetMapping("/employees")
	@PreAuthorize("hasRole('Role_User')")
	//@SecurityRequirement(name = "Bearer Authentication")
	public List<User> findAllEmployee(){
		return employeeService.findAllEmployees();
	}
	
	@PostMapping("/registerNewUser")
	public ResponseEntity<APIResponse> registerNewUser(@RequestBody UserRequestdto userRequestdto) {
		
		APIResponse apiResponse = userService.registerNewUser(userRequestdto);
		
		return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
	}
	
	@GetMapping("/employee/forAdmin")
	@PreAuthorize("hasRole('Role_Admin')")
	//@SecurityRequirement(name = "Bearer Authentication")
	public String forAdmin() {
		return "This URl is only accessible to admin";
	}
	
	@GetMapping("/employee/forUser")
	@PreAuthorize("hasRole('Role_User')")
	//@SecurityRequirement(name = "Bearer Authentication")
	public String forUser() {
		return "This URl is only accessible to the user";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<APIResponse> login(@RequestBody LoginRequestDto loginRequestDto) throws Exception{
		APIResponse apiResponse = jwtService.login(loginRequestDto);
		
		return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
	}
}
