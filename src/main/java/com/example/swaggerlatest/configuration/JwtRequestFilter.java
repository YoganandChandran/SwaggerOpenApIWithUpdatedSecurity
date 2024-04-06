package com.example.swaggerlatest.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.swaggerlatest.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	private com.example.swaggerlatest.service.JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			log.info("success.........................");
			final String header = request.getHeader("Authorization");

			String jwtToken = null;
			String userName = null;
			if (header != null && header.startsWith("Bearer ")) {
				jwtToken = header.substring(7);

				try {

					userName = jwtUtil.getUsername(jwtToken);
					log.info(userName + " username");

				} catch (IllegalArgumentException e) {
					System.out.println("Unable to get JWT token");
				} catch (ExpiredJwtException e) {
					System.out.println("Jwt token is expired");
				}
			} else {
				System.out.println("Jwt token doesnot start with Bearer da yoga");
			}

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userDetails = jwtService.loadUserByUsername(userName);
				try {
					if (jwtUtil.validateToken(jwtToken)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());

						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (SecurityContextHolder.getContext().getAuthentication() != null) {

				boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
						.anyMatch(r -> r.getAuthority().equals("ROLE_Role_Admin"));

				if (request.getRequestURI().contains("/employee/forAdmin") && !isAdmin) {

					response.setStatus(HttpServletResponse.SC_FORBIDDEN);
					return;
				}
			}

			filterChain.doFilter(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
