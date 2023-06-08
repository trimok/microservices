package com.sante.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author trimok
 *
 *         Security configuration
 */
@Configuration
@EnableWebFluxSecurity
@EnableWebSecurity
@Slf4j
public class GatewaySecurityConfig {

    /**
     * Configuring the filter chain
     * 
     * @param http : the http object
     * @return : SecurityFilterChain
     * @throws Exception : any exception
     */

    @Bean
    @Order(1)
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

	http.authorizeExchange(exchanges -> exchanges.anyExchange().hasRole("GATEWAY_USER"))
		.oauth2ResourceServer()
		.jwt()
		.jwtAuthenticationConverter(webFluxJwtAuthenticationConverter());

	// .oauth2Login();
	http.csrf().disable();
	return http.build();
    }

    @Bean
    public WebFluxJwtAuthenticationConverter webFluxJwtAuthenticationConverter() {
	return new WebFluxJwtAuthenticationConverter();
    }
}
