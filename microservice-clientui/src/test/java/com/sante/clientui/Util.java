package com.sante.clientui;

import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {

    private static String tokenTotal;
    private static String tokenPatient;
    private static String tokenHistory;
    private static String tokenNogateway;

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

    @SuppressWarnings("finally")
    public static String[] getTokens() {
	WebDriver driver = new ChromeDriver();
	driver.manage().window().minimize();
	driver.manage().window().setPosition(new Point(-30000, -30000));
	driver.get("http://localhost:8080/");

	try {
	    tokenTotal = getToken(driver, "total");
	    tokenPatient = getToken(driver, "patient");
	    tokenHistory = getToken(driver, "history");
	    tokenNogateway = getToken(driver, "nogateway");
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    driver.close();
	}

	return new String[] { tokenTotal, tokenPatient, tokenHistory, tokenNogateway };
    }

    @SuppressWarnings("finally")
    public static String getToken(WebDriver driver, String username) {

	String token = null;

	try {
	    WebElement element = driver.findElement(By.className("btn"));
	    new Actions(driver).moveToElement(new WebDriverWait(driver,
		    Duration.ofSeconds(10L))
		    .until(ExpectedConditions.elementToBeClickable(element)));

	    driver.findElement(By.id("username")).sendKeys(username);
	    driver.findElement(By.id("password")).sendKeys(username);
	    driver.findElement(By.className("btn")).click();

	    element = driver.findElement(By.className("logout"));
	    new Actions(driver)
		    .moveToElement(new WebDriverWait(driver, Duration.ofSeconds(10L))
			    .until(ExpectedConditions.elementToBeClickable(element)));

	    element = driver.findElement(By.id("token"));
	    token = element.getAttribute("value");
	    log.info("Token: " + token);

	    driver.findElement(By.className("logout")).click();

	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    return token;
	}
    }

    public static String getToken(String username) {
	return switch (username) {
	case "total" -> tokenTotal;
	case "patient" -> tokenPatient;
	case "history" -> tokenHistory;
	case "nogateway" -> tokenNogateway;
	default -> null;
	};
    }

    public static OidcUser getOidcUser(OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
	    ClientRegistrationRepository clientRegistrationRepository, String userName) {

	String token = getToken(userName);

	// Create OidcUser
	Map<String, Object> claims = new HashMap<>();
	claims.put("sub", userName);
	OidcIdToken oidcToken = new OidcIdToken(token, null, null, claims);
	OidcUserAuthority authority = new OidcUserAuthority(oidcToken);
	List<GrantedAuthority> listAuthorities = Arrays.asList(authority, new SimpleGrantedAuthority("SCOPE_openid"));
	OidcUser oidcUser = new DefaultOidcUser(listAuthorities, oidcToken, "sub");

	// Create registered OAuth2AuthorizedClient
	// (correctly managed by oAuth2AuthorizedClientService)
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
	// Registering authorizedClient
	oAuth2AuthorizedClientService.saveAuthorizedClient(authorizedClient, principal);

	return oidcUser;
    }

}
