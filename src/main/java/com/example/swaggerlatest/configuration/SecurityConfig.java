package com.example.swaggerlatest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.swaggerlatest.service.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;


	@Autowired
	private JwtService jwtService;

	 private static final RequestMatcher AUTH_WHITELIST_URLS = new OrRequestMatcher(
	            new AntPathRequestMatcher("/authenticate"),
	            new AntPathRequestMatcher("/registerNewUser"),
	            new AntPathRequestMatcher("/v3/api-docs/**"),
	            new AntPathRequestMatcher("/api-docs/**"),
	            new AntPathRequestMatcher("/swagger-ui.html"),
	            new AntPathRequestMatcher("/swagger-ui/**")
	    );


	    @Bean
		public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authManager)
				throws Exception {

			httpSecurity.csrf().disable();
	        httpSecurity.authorizeHttpRequests(
	                request -> request.requestMatchers(AUTH_WHITELIST_URLS).permitAll());

			httpSecurity.authorizeHttpRequests(request -> request.anyRequest().authenticated());
			httpSecurity.cors();
			httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
			httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
			httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			httpSecurity.authenticationManager(authManager);
			return httpSecurity.build();

		}




	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(jwtService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		AuthenticationManagerBuilder authManagerBuilder = httpSecurity
				.getSharedObject(AuthenticationManagerBuilder.class);

		authManagerBuilder.authenticationProvider(this.authenticationProvider());
		
		return authManagerBuilder.build();
	}

}
