package com.sante.clientui;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author trimok
 *
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "com.sante.clientui" })
public class ClientuiSecurityConfig {

    /**
     * Configuring the filter chain
     * 
     * @param http : the http object
     * @return : SecurityFilterChain
     * @throws Exception : any exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	http.authorizeHttpRequests()
		.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
		.anyRequest().hasAnyRole("USER")
		.and().formLogin()
		.permitAll()
		.and().csrf().disable();

	return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
	AuthenticationManagerBuilder authenticationManagerBuilder = http
		.getSharedObject(AuthenticationManagerBuilder.class);
	String password = passwordEncoder().encode("password");
	authenticationManagerBuilder.inMemoryAuthentication().withUser("user").password(password).roles("USER");
	return authenticationManagerBuilder.build();
    }

    /**
     * Password encoder
     * 
     * @return : a BCryptPasswordEncoder
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
    }
}
