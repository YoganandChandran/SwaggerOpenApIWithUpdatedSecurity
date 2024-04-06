package com.example.swaggerlatest.utils;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {

    private   String secret = "12345678990022345676788uii9998888";
    private   long expirationInMs = 3600 * 50;
    
    public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setExpirationInMs(long expirationInMs) {
		this.expirationInMs = expirationInMs;
	}


    public String generateToken( Authentication authentication, String clientSecret, long jwtExpirationInMs) {
		Map<String, Object> claims = new HashMap<>();
		Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
		List<String> userAuthorities = roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
		claims.put("authorities", String.join(",", userAuthorities));
		return doGenerateToken(authentication, claims, clientSecret, jwtExpirationInMs);
		
	}
	public String generateToken(Authentication authentication) {
		return this.generateToken(authentication, this.secret, this.expirationInMs);
	}

	public boolean validateToken(String authToken) throws Exception {
		return this.validateToken(authToken, this.secret);
	}
	public boolean validateToken(String authToken, String clientSecret) throws Exception {
		boolean isValidToken = false;
		try {
			Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(this.getSecretAsBytes(clientSecret)).build()
					.parseClaimsJws(authToken);
			isValidToken = true;
            System.out.println("Issue Date -> " +claims.getBody().getIssuedAt());
            System.out.println("Expiry Date -> " +claims.getBody().getExpiration());
		} catch (MalformedJwtException | SignatureException |UnsupportedJwtException | IllegalArgumentException e) {
			throw new Exception("Invalid JWT Token", e);
		}
		return isValidToken;
	}

	public String getUsername(String authToken) {
		return this.getUsername(authToken, this.secret);
	}
	
	public String getUsername(String authToken, String clientSecret) {
		Claims claims = Jwts.parserBuilder().setSigningKey(this.getSecretAsBytes(clientSecret)).build().parseClaimsJws(authToken)
				.getBody();
		return claims.getSubject();
	}

	public List<SimpleGrantedAuthority> getGrantedAuthorities(String authToken) {
		return this.getGrantedAuthorities(authToken, this.secret);
	}
	
	public List<SimpleGrantedAuthority> getGrantedAuthorities(String authToken, String clientSecret) {
		Claims claims = Jwts.parserBuilder().setSigningKey(this.getSecretAsBytes(clientSecret)).build().parseClaimsJws(authToken)
				.getBody();
		String authorities = claims.get("authorities", String.class);
		List<SimpleGrantedAuthority> roles = new LinkedList<>();
		if ( null != authorities && !StringUtils.isBlank(authorities) && authorities.contains(",")) {
			roles = java.util.Arrays.asList(authorities.split(",")).stream()
					.map(authority -> new SimpleGrantedAuthority("ROLE" + authority)).collect(Collectors.toList());
		} else if ( null != authorities && !StringUtils.isBlank(authorities) ) {
			roles.add(new SimpleGrantedAuthority(authorities));
		}
 
		return roles;
 
	}
	private String doGenerateToken(Authentication authentication, Map<String, Object> claims,String clientSecret, long jwtExpirationInMs) {
		long currentTime = System.currentTimeMillis();
		JwtBuilder jwtBuilder = Jwts.builder();
		return jwtBuilder.setClaims(claims).setSubject(authentication.getName()).setIssuedAt(new Date(currentTime))
				.setExpiration(new Date(currentTime + jwtExpirationInMs))
				.signWith(SignatureAlgorithm.HS256, this.getSecretAsBytes(clientSecret)).compact();
	}
	
	private byte[] getSecretAsBytes(String clientSecret) {
		return clientSecret.getBytes();
	}
}
