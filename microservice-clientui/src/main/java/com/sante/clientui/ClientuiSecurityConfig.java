package com.sante.clientui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author trimok
 *
 *         Basic security configuration
 */
@ComponentScan(basePackages = { "com.sante.clientui" })
@Configuration
public class ClientuiSecurityConfig {

    @Value("${logout.success.url}")
    String logoutSuccessUrl;

    /**
     * @param http : http
     * @return : a SecurityFilterChain object
     * @throws Exception : exception raised
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	http
		.authorizeHttpRequests(authorize -> authorize
			.anyRequest().authenticated())
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.clearAuthentication(true).logoutSuccessUrl(logoutSuccessUrl)
		.deleteCookies("JSESSIONID").invalidateHttpSession(true).permitAll()
		.and()
		.oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/myoauth2"))
		.oauth2Client(Customizer.withDefaults());
	return http.build();
    }
}
