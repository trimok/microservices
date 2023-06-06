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

@Configuration
@Slf4j
public class OAuthFeignConfig {

    public static final String CLIENT_REGISTRATION_ID = "myoauth2";

    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuthFeignConfig(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
	    ClientRegistrationRepository clientRegistrationRepository) {
	this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
	this.clientRegistrationRepository = clientRegistrationRepository;
    }

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
	    String token = "";
	    if (client == null) {
		throw new IllegalStateException("client credentials flow on " + clientRegistration.getRegistrationId()
			+ " failed, client is null");
	    } else {
		token = client.getAccessToken().getTokenValue();
	    }
	    requestTemplate.header("Authorization", "Bearer " + token);
	};
    }

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