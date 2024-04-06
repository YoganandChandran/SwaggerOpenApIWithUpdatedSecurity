package com.example.swaggerlatest.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.swaggerlatest.entity.APIResponse;
import com.example.swaggerlatest.entity.LoginRequestDto;
import com.example.swaggerlatest.entity.User;
import com.example.swaggerlatest.repository.LoginDao;
import com.example.swaggerlatest.repository.UserDao;
import com.example.swaggerlatest.utils.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LoginDao loginDao;

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private AuthenticationManager authenticationManager;

//	public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
//		
//		String userName = jwtRequest.getUserName();
//		String userPassword = jwtRequest.getUserPassword();
//		authenticate(userName, userPassword);
//		
//		System.out.println(jwtRequest);
//		
//		final UserDetails userDetails = loadUserByUsername(userName);
//		
//		
//		
//		 User user = (User) userDao.findByUserName(userName);
//		
//		String newGeneratedToken = jwtUtil.generateToken(userDetails,user.getPractice());
//		
//		System.out.println(newGeneratedToken);
//		
//		return new JwtResponse(newGeneratedToken);
//		
//
//	}
	
	public APIResponse login(LoginRequestDto loginRequestDto) throws Exception {
		
		APIResponse apiResponse = new APIResponse();
		//System.out.println(loginRequestDto);
		
		//User user = loginDao.findOneByUserNameAndUserPassword(loginRequestDto.getUserName(),loginRequestDto.getUserPassword());
		
		//User user1 = loginDao.findByUserName(loginRequestDto.getUserName());
		
		//System.out.println(user1);
		
		String userName = loginRequestDto.getUserName();
		String password = loginRequestDto.getUserPassword();
		
		log.info(password+"......................nksdk");
		
		Authentication authentication =  authenticate(userName, password);
		
		
		
		System.out.println(userName +"/n"+password);
		
		//System.out.println(loginRequestDto.getUserName()+loginRequestDto.getUserPassword());
		//System.out.println();
		
//		if(user == null) {
//			apiResponse.setData("User login fail");
//			return apiResponse;
//		}
		
		//final UserDetails userDetails = loadUserByUsername(userName);
		
		User user = (User) userDao.findByUserName(userName);
		
		System.out.println(user);
		
		String token = jwtUtil.generateToken(authentication);
		

		Map<String, Object> data = new HashMap<>();

		data.put("accessToken", token);

		apiResponse.setData(data);

		return apiResponse;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userDao.findByUserName(username);
		
		log.info(username);
		
		System.out.println(username+"Username daaaaaaaaaa");
		
		if(user != null) {
			return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(), getAuthorities(user) );
		}else {
			
			throw new UsernameNotFoundException("Username is not valid "+username);
		}
	}

	
	private Set getAuthorities(User user) {
		Set<SimpleGrantedAuthority> authorites = new HashSet<>();
		
		user.getRoles().forEach(role -> {
			authorites.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
		});
		
		return authorites;
	}
	
	
	private Authentication authenticate(String userName, String userPassword) throws Exception {
		log.info(userPassword+"..............suc");
		
		try {
			Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, userPassword));
			return authentication;
		}
		catch(DisabledException disabledException) {
			throw new Exception("User is Disabled",disabledException);
		} catch(BadCredentialsException badCredentialsException) {
			throw new Exception("Bad credential from user",badCredentialsException);
		}
	}
}
