package com.sante.clientui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import com.sante.clientui.controller.ClientuiController;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ClientuiFeignConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public RequestInterceptor requestInterceptor() {
	return requestTemplate -> {

	    ClientuiController controller = applicationContext.getBean(ClientuiController.class);
	    OAuth2AuthorizedClient authorizedClient = controller.getAuthorizedClient();

	    String token = authorizedClient.getAccessToken().getTokenValue();
	    requestTemplate.header("Authorization", "Bearer " + token);
	};
    }

    /*
     * public static final String CLIENT_REGISTRATION_ID = "client";
     * 
     * private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
     * private final ClientRegistrationRepository clientRegistrationRepository;
     * 
     * public ClientuiFeignConfig(OAuth2AuthorizedClientService
     * oAuth2AuthorizedClientService, ClientRegistrationRepository
     * clientRegistrationRepository) { this.oAuth2AuthorizedClientService =
     * oAuth2AuthorizedClientService; this.clientRegistrationRepository =
     * clientRegistrationRepository; }
     * 
     * @Bean public RequestInterceptor requestInterceptor() { return requestTemplate
     * -> { ClientRegistration clientRegistration = clientRegistrationRepository
     * .findByRegistrationId(CLIENT_REGISTRATION_ID);
     * ClientuiCredentialsFeignManager clientCredentialsFeignManager = new
     * ClientuiCredentialsFeignManager( authorizedClientManager(),
     * clientRegistration); String token =
     * clientCredentialsFeignManager.getAccessToken();
     * requestTemplate.header("Authorization", "Bearer " + token); }; }
     * 
     * @Bean OAuth2AuthorizedClientManager authorizedClientManager() {
     * OAuth2AuthorizedClientProvider authorizedClientProvider =
     * OAuth2AuthorizedClientProviderBuilder.builder() .clientCredentials()
     * .build();
     * 
     * AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager
     * = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
     * clientRegistrationRepository, oAuth2AuthorizedClientService);
     * authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider)
     * ; return authorizedClientManager; }
     */
}