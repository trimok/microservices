package com.sante.oauth2server;

import static org.springframework.security.config.Customizer.withDefaults;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author trimok
 *
 *
 *         Security configuration
 */
@Configuration

@Slf4j
public class Oauth2serverSecurityConfig {

    /**
     * Configuration oauth2server
     * 
     * @param http : http
     * @return : SecurityFilterChain
     * @throws Exception : exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

	OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

	return http
		.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(withDefaults())
		.and()
		.exceptionHandling(e -> e
			.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")))
		.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
		.build();

    }

    /**
     * Standard security access
     * 
     * @param http : http
     * @return : SecurityFilterChain
     * @throws Exception : exception
     */
    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
	return http
		.formLogin(withDefaults())
		.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
		.build();

    }

    /**
     * User list (in memory)
     * 
     * @return UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
	var total = User.withUsername("total")
		.password("total")
		.roles("GATEWAY_USER", "PATIENT_USER", "PATIENT_HISTORY_USER", "EXPERT_USER")
		.build();
	var patient = User.withUsername("patient")
		.password("patient")
		.roles("GATEWAY_USER", "PATIENT_USER")
		.build();

	var history = User.withUsername("history")
		.password("history")
		.roles("GATEWAY_USER", "PATIENT_USER", "PATIENT_HISTORY_USER")
		.build();

	return new InMemoryUserDetailsManager(total, patient, history);
    }

    /**
     * @return : PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
	return NoOpPasswordEncoder.getInstance();
    }

    /**
     * Enregistrement du client (InMemoryRegisteredClientRepository)
     * 
     * @return : RegisteredClientRepository
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
	RegisteredClient registeredClient = RegisteredClient.withId("myoauth2")
		.clientId("client")
		.clientSecret("secret")
		.scope(OidcScopes.OPENID)
		.redirectUri("http://127.0.0.1:8080/login/oauth2/code/myoauth2")
		.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
		.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		.build();

	return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * Associate roles to claim
     * 
     * @return : OAuth2TokenCustomizer
     */
    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
	return context -> {
	    if (context.getTokenType().getValue().equals("access_token")) {
		Authentication principal = context.getPrincipal();
		Set<String> authorities = principal.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.toSet());
		context.getClaims().claim("roles", authorities);
	    }
	};
    }

    /**
     * Gives an AuthorizationServerSettings object
     * 
     * @return : AuthorizationServerSettings
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
	return AuthorizationServerSettings.builder().build();
    }

    /**
     * Gives an JwtDecoder object
     * 
     * @param jwkSource : jwkSource
     * @return : jwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
	return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * Gives a JWKSource object
     * 
     * @return : JWKSource
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
	RSAKey rsaKey = generateRsa();
	JWKSet jwkSet = new JWKSet(rsaKey);
	return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /**
     * Gives an RSAKey
     * 
     * @return : RSAKey
     */
    public static RSAKey generateRsa() {
	KeyPair keyPair = generateRsaKey();
	RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
	RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
	return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
    }

    /**
     * Gives an KeyPair object
     * 
     * @return : KeyPair
     */
    static KeyPair generateRsaKey() {
	KeyPair keyPair;
	try {
	    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
	    keyPairGenerator.initialize(2048);
	    keyPair = keyPairGenerator.generateKeyPair();
	} catch (Exception ex) {
	    throw new IllegalStateException(ex);
	}
	return keyPair;
    }
}