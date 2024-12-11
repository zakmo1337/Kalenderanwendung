package com.example.demo;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	// Provides a convenient base class for creating a WebSecurityConfigurer
	// instance.
	// The implementation allows customization by overriding methods.
	@Autowired
	private DataSource dataSource;

	@Bean
	public UserDetailsService userDetailsService() { // Core interface which loads user-specific data. It is used
														// throughout the framework as a user DAO and is the strategy
														// used by the DaoAuthenticationProvider.

		return new CustomUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() { // Implementation of PasswordEncoder that uses the BCrypt strong
														// hashing function. Clientscan optionally supply a "version"
														// ($2a, $2b, $2y) and a "strength" (a.k.a. log roundsin BCrypt)
														// and a SecureRandom instance. The larger the strength
														// parameter the more workwill have to be done (exponentially)
														// to hash the passwords. The default value is 10.
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() { // An AuthenticationProvider implementation that
																// retrieves user details from a UserDetailsService.
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception { // SecurityBuilder used to create an
																					// AuthenticationManager. Allows
																					// foreasily building in memory
																					// authentication, LDAP
																					// authentication, JDBC
																					// basedauthentication, adding
																					// UserDetailsService, and adding
																					// AuthenticationProvider's.
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/meetings").authenticated().antMatchers("/h2-console/**", "/calendar")
				.permitAll().anyRequest().permitAll().and().formLogin().usernameParameter("email")
				.defaultSuccessUrl("/meetings").permitAll().and().logout().logoutSuccessUrl("/").permitAll();

		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
