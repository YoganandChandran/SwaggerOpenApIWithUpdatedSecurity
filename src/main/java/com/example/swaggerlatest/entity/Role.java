package com.example.swaggerlatest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="roles")
public class Role {
	
	@Id
	private String roleName;
	private String roleDescription;

}
