package com.sante.clientui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author trimok Feign client configuration
 */
@Configuration
@Slf4j
public class OAuthFeignConfig {

    /**
     * CLIENT_REGISTRATION_ID
     */
    public static final String CLIENT_REGISTRATION_ID = "myoauth2";

    /**
     * oAuth2AuthorizedClientService
     */
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    /**
     * clientRegistrationRepository
     */
    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * @param oAuth2AuthorizedClientService : oAuth2AuthorizedClientService
     * @param clientRegistrationRepository  : clientRegistrationRepository
     */
    public OAuthFeignConfig(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
	    ClientRegistrationRepository clientRegistrationRepository) {
	this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
	this.clientRegistrationRepository = clientRegistrationRepository;
    }

    /**
     * Interceptor to relay token
     * 
     * @return : RequestInterceptor
     */
    @Bean
    public RequestInterceptor requestInterceptor() {

	ClientRegistration clientRegistration = clientRegistrationRepository
		.findByRegistrationId(CLIENT_REGISTRATION_ID);

	return requestTemplate -> {

	    OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
		    .withClientRegistrationId(clientRegistration.getRegistrationId())
		    .principal(SecurityContextHolder.getContext().getAuthentication())
		    .build();

	    OAuth2AuthorizedClient client = authorizedClientManager().authorize(oAuth2AuthorizeRequest);
	    String token = null;
	    if (client == null) {
		throw new IllegalStateException("client credentials flow on "
			+ clientRegistration.getRegistrationId()
			+ " failed, client is null");
	    } else {
		token = client.getAccessToken().getTokenValue();
	    }

	    if (token != null) {
		requestTemplate.header("Authorization", "Bearer " + token);
	    }

	    log.info("Headers request :" + requestTemplate.headers().toString());
	};
    }

    /**
     * Get a manager to authorize request
     * 
     * @return : OAuth2AuthorizedClientManager
     */
    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager() {
	OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
		.clientCredentials()
		.build();

	AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
		clientRegistrationRepository, oAuth2AuthorizedClientService);
	authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
	return authorizedClientManager;
    }
}