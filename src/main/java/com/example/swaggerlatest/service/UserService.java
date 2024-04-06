package com.example.swaggerlatest.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.swaggerlatest.entity.APIResponse;
import com.example.swaggerlatest.entity.Role;
import com.example.swaggerlatest.entity.User;
import com.example.swaggerlatest.entity.UserRequestdto;
import com.example.swaggerlatest.repository.RoleDao;
import com.example.swaggerlatest.repository.UserDao;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleDao roleDao;

	static int i = 0;

	public APIResponse registerNewUser(UserRequestdto userRequestdto) {

		APIResponse apiResponse = new APIResponse();

		User user = new User();

		Role role = roleDao.findByRoleName("Role_User");

		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setRoles(roles);

		user.setUserName(userRequestdto.getUserName());
		user.setUserFirstName(userRequestdto.getUserFirstName());
		user.setUserLastName(userRequestdto.getUserLastName());
		user.setPractice(userRequestdto.getPractice());
		user.setUserPassword(getEncodedPassword(userRequestdto.getUserPassword()));

//		if(!userDao.existsByuserName(user.getUserName())) {
//			throw new UserAlreadyExistsException("User name is already Exits please change user name");
//		}

		user = userDao.save(user);

		apiResponse.setStatus(200);
		apiResponse.setData("Registered successfully");
		apiResponse.setError(HttpStatus.OK);

		return apiResponse;
	}

	
//	public void initRolesAndUser() {
//		Role adminRole = new Role();
//		adminRole.setRoleName("Admin");
//		adminRole.setRoleDescription("Admin Role");
//		roleDao.save(adminRole);
//
//		Role userRole = new Role();
//		userRole.setRoleName("User");
//		userRole.setRoleDescription("Default Role for newly created record");
//		roleDao.save(userRole);
//
//		User adminUser = new User();
//		adminUser.setUserFirstName("admin");
//		adminUser.setUserLastName("admin");
//		adminUser.setUserName("admin123");
//		adminUser.setPractice("Java");
//		adminUser.setUserPassword(getEncodedPassword("admin@pass"));
//
//		Set<Role> adminRoles = new HashSet<>();
//		adminRoles.add(adminRole);
//
//		adminUser.setRoles(adminRoles);
//		userDao.save(adminUser);
//
////		User user = new User();
////		user.setUserFirstName("raj");
////		user.setUserLastName("sharma");
////		user.setUserName("raj123");
////		user.setUserPassword(getEncodedPassword("raj@pass"));
////		
////		Set<Role> userRoles = new HashSet<>();
////		userRoles.add(userRole);
////		
////		user.setRoles(userRoles);
////		userDao.save(user);
//	}

	public String getEncodedPassword(String password) {

		return passwordEncoder.encode(password);
	}
}
