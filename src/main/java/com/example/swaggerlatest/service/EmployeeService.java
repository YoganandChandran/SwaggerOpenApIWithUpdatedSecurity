package com.example.swaggerlatest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.swaggerlatest.entity.User;
import com.example.swaggerlatest.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	public User saveEmployee(User employee) {
		
		 return employeeRepository.save(employee);
		
	}
	
	public Optional<User> getEmployeeById(Integer id) {
		
		return employeeRepository.findById(id);
	}
	
	public List<User> findAllEmployees(){
		return employeeRepository.findAll();
	}
}
