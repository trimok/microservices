package com.sante.tokenutility.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author trimok
 *
 *         The controller of the application
 */
@Controller
public class ClientuiController {

    /**
     * Entry point of the application, return list patients view (home)
     * 
     * @param ra    : the redirect attributes
     * @param model : the model
     * @return : string
     */
    @GetMapping("/")
    public String viewHomePageWithToken(RedirectAttributes ra, Model model,
	    @RegisteredOAuth2AuthorizedClient("myoauth2") OAuth2AuthorizedClient client) {
	String token = null;

	if (client != null && client.getAccessToken() != null) {
	    token = client.getAccessToken().getTokenValue();
	} else {
	    token = "user or user id token null";
	}

	model.addAttribute("token", token);

	return "home";
    }

}
