package com.sante.clientui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class ClientuiSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	http.authorizeHttpRequests().anyRequest().authenticated().and().oauth2Login();
	http.csrf().disable();
	return http.build();
    }

    /*
     * @Bean public RequestInterceptor requestTokenBearerInterceptor() {
     * 
     * return new RequestInterceptor() {
     * 
     * @Override public void apply(RequestTemplate requestTemplate) { Authentication
     * authentication = SecurityContextHolder.getContext().getAuthentication(); //
     * OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) //
     * authentication.getDetails(); AbstractAuthenticationToken details =
     * (AbstractAuthenticationToken) ((OAuth2AuthenticationToken) authentication)
     * .getDetails(); requestTemplate.header("Authorization", "Bearer " + details);
     * log.info(authentication.toString()); } }; }
     */
}
