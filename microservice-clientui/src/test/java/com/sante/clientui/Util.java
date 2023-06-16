package com.sante.clientui;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sante.clientui.model.Patient;

public class Util {

    public static class Mapper extends ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;

	/**
	 * 
	 */
	public Mapper() {
	    // Management for the LocalDate serialization / deserialization
	    this.registerModule(new JavaTimeModule());
	}
    }

    public static Mapper mapper = new Mapper();

    public static Patient getPatientFromMvcResult(MvcResult mvcResult)
	    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
	String json = mvcResult.getResponse().getContentAsString();
	Patient patient = mapper.readValue(json, Patient.class);
	return patient;
    }

    public static List<Patient> getListPatientFromMvcResult(MvcResult mvcResult)
	    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
	String json = mvcResult.getResponse().getContentAsString();
	List<Patient> patients = new Mapper().readValue(json, new TypeReference<List<Patient>>() {
	});
	return patients;
    }

    public static String getTotalToken() {
	return "eyJraWQiOiJiNDlmMGVlYS05OWM2LTRmOTItYjRlYS0yMThmMGU1ZDYzNmUiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0b3RhbCIsImF1ZCI6ImNsaWVudCIsIm5iZiI6MTY4NjkwOTE3NCwic2NvcGUiOlsib3BlbmlkIl0sInJvbGVzIjpbIlJPTEVfRVhQRVJUX1VTRVIiLCJST0xFX1BBVElFTlRfVVNFUiIsIlJPTEVfUEFUSUVOVF9ISVNUT1JZX1VTRVIiLCJST0xFX0dBVEVXQVlfVVNFUiJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2OTU1NDkxNzQsImlhdCI6MTY4NjkwOTE3NH0.plQchwTVsO5ttlN3uL5k4EidfkAeWNBCSO0Aiqz37lUpl4HbuuK3-9GBzIuRUs9WxbQAPjbsclXDxlfO75Wl2xg1rublSDLXaiUsYBzOhrE9_x1MTgM8NW_n7oGqDbOX2zc6-yqYoFC7lqjfWpDreL4yg2pS9A-OU-HM2gtCGFV8ZvTQz62oULfT2IGRZ2Uouq0HYQUBqLxO-PTlkn0mUUuRYbxCKTboA4S88D3VzafGXJhBBR82Roi4knmOnWAT8qT8mu0nFThxYeIxLy0DNIpkSS6HemI0Mkjj7VBuRYjvgLgPnznhJaPm2ObX7JJTpSdKPEkx3qDGjQDZFArs3A";
    }

    public static String getPatientToken() {
	return "eyJraWQiOiIzNzA2YzM0MC0yMjRjLTQzNzQtOWM3YS0wMTU2YjYyMjhlMmQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJwYXRpZW50IiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjg2ODI2MzkzLCJzY29wZSI6WyJvcGVuaWQiXSwicm9sZXMiOlsiUk9MRV9QQVRJRU5UX1VTRVIiLCJST0xFX0dBVEVXQVlfVVNFUiJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2OTU0NjYzOTMsImlhdCI6MTY4NjgyNjM5M30.BzATHg9ah-3l64F8Y0X-pd_X5yWVNJoQOZQmA7-Kyu58jafLYuzvQ5wk4KIk0HnjKY3q71rvaOFB_1RYAApZ5Mxod_sc5rIOFu4B8hXxqiOW_aje1hiDE-cITaEYD2mLf_lBckAxY4VK9HRBpn0zD-iKAg1rVm9Dpnkg4zrg5D6bi6DMVnoGR33obrCzgyaQok8xIn_klliHHQPt4tqhqT_Acv-atfNGz_CGT1GlctvHPkuoi8WA71134T-YyPGcEQUQISoxyaa_BqrfI4ZYJ2j5cvoZiSvRwNzN-1qA-s8BFFZ2UEELKTC17SbOGupM9iQkAdnrscLq_mP8TBlOTQ";
    }

    public static String getHistoryToken() {
	return "eyJraWQiOiIzNzA2YzM0MC0yMjRjLTQzNzQtOWM3YS0wMTU2YjYyMjhlMmQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJoaXN0b3J5IiwiYXVkIjoiY2xpZW50IiwibmJmIjoxNjg2ODI2NDY1LCJzY29wZSI6WyJvcGVuaWQiXSwicm9sZXMiOlsiUk9MRV9QQVRJRU5UX1VTRVIiLCJST0xFX1BBVElFTlRfSElTVE9SWV9VU0VSIiwiUk9MRV9HQVRFV0FZX1VTRVIiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwIiwiZXhwIjoxNjk1NDY2NDY1LCJpYXQiOjE2ODY4MjY0NjV9.dq0T46flY8mVsBSQ8LI_h5LbHj_-acP6Yp6TtADQZXenkCE1K7Ds_J6GmCjMHgH1a4yKbMUiIgoXptgGtFWbUs5-th9ajmSdyBKpt5CXUucheg582A3z-Q1me73dMk0Zys7Dy771xaVpjt2Kcmr7JenhbPtCEPZuv22au8-fqoOJ6rw4uZ6oDGiNsFJBKJOwhys8kXSj---JWUkKhqVlrdHdd4MWiMD7ZLWFm5JoiQLKygUSdzBpj9zaTo7Ty101E75jxwgZjP3qbna784B7zDaiWCkATrEe_My51vIqTZwBwS3UIAaKzbDANp6xkGmwQypDZyHWWScu-zZJKPPr5g";
    }

    public static String getNogatewayToken() {
	return "eyJraWQiOiIzNzA2YzM0MC0yMjRjLTQzNzQtOWM3YS0wMTU2YjYyMjhlMmQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJub2dhdGV3YXkiLCJhdWQiOiJjbGllbnQiLCJuYmYiOjE2ODY4MjY1NTcsInNjb3BlIjpbIm9wZW5pZCJdLCJyb2xlcyI6WyJST0xFX0VYUEVSVF9VU0VSIiwiUk9MRV9QQVRJRU5UX1VTRVIiLCJST0xFX1BBVElFTlRfSElTVE9SWV9VU0VSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTAwMCIsImV4cCI6MTY5NTQ2NjU1NywiaWF0IjoxNjg2ODI2NTU3fQ.XtGgkaUcs7zYtu1l8GwkUN0SnNnPGPCc6gKGIpA4LzqcLpiXdo54a5TCreBH7H4O6oPR_cXoyCsofH8MXHzXZO7vLS43RV1Q6tUuwnduWSKVtuU14YIh7hsGzDMKTOBMZqOlYR8SLm7yBoeL45p5j9W4XFTApd2gkE2Xtb-UPdPt48R1h8-9zWGSlQgPaAmE8XMSrV8hsqYM8OvqDyXXxEPzEcT5B9necDPE4NdH7JFHb9mExywex1YetSXN5HOHYF7La2WxMfRb9znXV_MKj9DJbw6FQVpLE9P0innK0MNPPTUX8HcBJQXVNWSRsE1l6BW4LiJkEwXYmv_zjYzfVw";
    }

    public static String getToken(String userName) {
	return switch (userName) {
	case "total" -> getTotalToken();
	case "patient" -> getPatientToken();
	case "history" -> getHistoryToken();
	case "nogateway" -> getNogatewayToken();
	default -> getTotalToken();
	};
    }

    public static OidcUser getOidcUser(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
	    ClientRegistrationRepository clientRegistrationRepository, String userName) {

	String token = Util.getToken(userName);

	Map<String, Object> claims = new HashMap<>();
	claims.put("sub", userName);
	OidcIdToken oidcToken = new OidcIdToken(token, null, null, claims);
	OidcUserAuthority authority = new OidcUserAuthority(oidcToken);
	List<GrantedAuthority> listAuthorities = Arrays.asList(authority, new SimpleGrantedAuthority("SCOPE_openid"));
	OidcUser oidcUser = new DefaultOidcUser(listAuthorities, oidcToken, "sub");

	OAuth2AccessToken oauth2Token = new OAuth2AccessToken(TokenType.BEARER, token, null, null);
	ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("myoauth2");
	OAuth2AuthorizedClient authorizedClient = new OAuth2AuthorizedClient(clientRegistration, userName, oauth2Token);
	Authentication principal = new Authentication() {

	    @Override
	    public String getName() {
		return userName;
	    }

	    @Override
	    public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	    }

	    @Override
	    public Object getCredentials() {
		return null;
	    }

	    @Override
	    public Object getDetails() {
		return null;
	    }

	    @Override
	    public Object getPrincipal() {
		return null;
	    }

	    @Override
	    public boolean isAuthenticated() {
		return true;
	    }

	    @Override
	    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
	    }

	};
	oAuth2AuthorizedClientService.saveAuthorizedClient(authorizedClient, principal);

	return oidcUser;
    }
}
