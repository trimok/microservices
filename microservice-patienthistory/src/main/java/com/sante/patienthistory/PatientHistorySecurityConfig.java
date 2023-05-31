package com.sante.patienthistory;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author trimok
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = { "com.sante.patient" })
public class PatientHistorySecurityConfig {

    /**
     * Configuring the filter chain
     * 
     * @param http : the http object
     * @return : SecurityFilterChain
     * @throws Exception : any exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	http.authorizeRequests(authorize -> authorize.anyRequest().hasAnyRole("GATEWAY_USER"))
		.oauth2ResourceServer().jwt()
		.jwtAuthenticationConverter(jwtAuthenticationConverterForKeycloak());
	;

	return http.build();
    }

    /**
     * This converter extracts the list of user roles from a "roles" claim and
     * builds a list of GrantedAuthority using the "ROLE_" prefix.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
	var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
	jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
	jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

	var jwtAuthenticationConverter = new JwtAuthenticationConverter();
	jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

	return jwtAuthenticationConverter;
    }

    /**
     * By default, Keycloak assigns user roles to a "roles" object within the
     * "realm_access" claim. This converter extracts the list of user roles from
     * "realm.access.roles" and builds a list of GrantedAuthority using the "ROLE_"
     * prefix.
     */
    @SuppressWarnings("unused")
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
	Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = null;
	jwtGrantedAuthoritiesConverter = jwt -> {
	    Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
	    if (realmAccess == null) {
		return null;
	    } else {
		Collection<String> roles = realmAccess.get("roles");
		return roles.stream()
			.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
			.collect(Collectors.toList());
	    }
	};

	var jwtAuthenticationConverter = new JwtAuthenticationConverter();
	if (jwtGrantedAuthoritiesConverter != null) {
	    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
	}

	return jwtAuthenticationConverter;
    }
}
