package com.sante.gateway;

import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import reactor.core.publisher.Mono;

/**
 * JWT converter that takes the roles from 'groups' claim of JWT token.
 */
/**
 * @author trimok
 * 
 *         uthenticationConverter mode webflux
 *
 */
public class WebFluxJwtAuthenticationConverter
	implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    /**
     * ROLE_CLAIM
     */
    private static final String ROLE_CLAIM = "roles";
    /**
     * ROLE_PREFIX
     */
    private static final String ROLE_PREFIX = "";

    /**
     * defaultAuthoritiesConverter
     */
    private final Converter<Jwt, Collection<GrantedAuthority>> defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    /**
     * Conversion
     */
    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
	return Mono.just(extractAuthorities(jwt))
		.map((authorities) -> new JwtAuthenticationToken(jwt, authorities));

    }

    /**
     * Extraction authorities
     * 
     * @param jwt : jwt
     * @return : Collection<GrantedAuthority>
     */
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
	Collection<GrantedAuthority> authorities = this.getRoles(jwt).stream()
		.map(authority -> ROLE_PREFIX + authority.toUpperCase())
		.map(SimpleGrantedAuthority::new)
		.collect(Collectors.toSet());

	authorities.addAll(defaultGrantedAuthorities(jwt));

	return authorities;
    }

    /**
     * defaultGrantedAuthorities
     * 
     * @param jwt : jwt
     * @return : Collection<GrantedAuthority>
     */
    private Collection<GrantedAuthority> defaultGrantedAuthorities(Jwt jwt) {
	return Optional.ofNullable(defaultAuthoritiesConverter.convert(jwt))
		.orElse(emptySet());
    }

    /**
     * Getting roles
     * 
     * @param jwt : jwt
     * @return : roles
     */
    @SuppressWarnings("unchecked")
    private Collection<String> getRoles(Jwt jwt) {
	Object scopes = jwt.getClaims().get(ROLE_CLAIM);
	if (scopes instanceof Collection) {
	    return (Collection<String>) scopes;
	}

	return Collections.emptyList();
    }
}
