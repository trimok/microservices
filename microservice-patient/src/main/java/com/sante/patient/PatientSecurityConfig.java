package com.sante.patient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author trimok
 *
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class PatientSecurityConfig {

    /**
     * Configuring the filter chain
     * 
     * @param http : the http object
     * @return : SecurityFilterChain
     * @throws Exception : any exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	http.authorizeRequests(authorize -> authorize.anyRequest().hasRole("PATIENT_USER"))
		.oauth2ResourceServer().jwt()
		.jwtAuthenticationConverter(jwtAuthenticationConverterForRoles());
	;

	return http.build();
    }

    /**
     * This converter extracts the list of user roles from a "roles" claim and
     * builds a list of GrantedAuthority
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
	var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
	jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
	jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

	var jwtAuthenticationConverter = new JwtAuthenticationConverter();
	jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

	return jwtAuthenticationConverter;
    }

    @SuppressWarnings("unused")
    public JwtAuthenticationConverter jwtAuthenticationConverterForRoles() {
	Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = null;
	jwtGrantedAuthoritiesConverter = jwt -> {
	    Collection<String> roles = jwt.getClaim("roles");
	    log.info("Roles " + roles.toString());
	    if (roles == null) {
		roles = new ArrayList<String>();
	    }
	    return roles.stream()
		    .map(role -> new SimpleGrantedAuthority(role))
		    .collect(Collectors.toList());

	};

	var jwtAuthenticationConverter = new JwtAuthenticationConverter();
	if (jwtGrantedAuthoritiesConverter != null) {
	    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
	}

	return jwtAuthenticationConverter;
    }
}
