package com.sante.clientui;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientuiCredentialsFeignManager {
    /*
     * 
     * private final OAuth2AuthorizedClientManager manager; private final
     * Authentication principal; private final ClientRegistration
     * clientRegistration;
     * 
     * public ClientuiCredentialsFeignManager(OAuth2AuthorizedClientManager manager,
     * ClientRegistration clientRegistration) { this.manager = manager;
     * this.clientRegistration = clientRegistration; this.principal =
     * createPrincipal(); }
     * 
     * private Authentication createPrincipal() { Authentication authentication =
     * new Authentication() {
     * 
     * @Override public Collection<? extends GrantedAuthority> getAuthorities() {
     * return Collections.emptySet(); }
     * 
     * @Override public Object getCredentials() { return null; }
     * 
     * @Override public Object getDetails() { return null; }
     * 
     * @Override public Object getPrincipal() { return this; }
     * 
     * @Override public boolean isAuthenticated() { return false; }
     * 
     * @Override public void setAuthenticated(boolean isAuthenticated) throws
     * IllegalArgumentException { }
     * 
     * @Override public String getName() { return clientRegistration.getClientId();
     * } };
     * 
     * return authentication; }
     * 
     * public String getAccessToken() { try { OAuth2AuthorizeRequest
     * oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
     * .withClientRegistrationId(clientRegistration.getRegistrationId())
     * .principal(principal).build(); OAuth2AuthorizedClient client =
     * manager.authorize(oAuth2AuthorizeRequest); if (client == null) { throw new
     * IllegalStateException("client credentials flow on " +
     * clientRegistration.getRegistrationId() + " failed, client is null"); } return
     * client.getAccessToken().getTokenValue(); } catch (Exception exp) {
     * log.error("client credentials error " + exp.getMessage()); } return null; }
     */
}
