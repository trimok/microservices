package com.sante.patient;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * @author trimok
 *
 *         Configuration Swagger / Open API
 *
 */
@Configuration
public class SwaggerConfig {

    /**
     * @return : OpenAPI
     */
    @Bean
    public OpenAPI openAPI() {
	return new OpenAPI()
		.info(new Info()
			.title("Microservice patient")
			.description("Description des endpoints de type CRUD.")
			.version("v1.0.0"));
    }

    /**
     * @return : GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi userApi() {
	return GroupedOpenApi.builder()
		.group("user")
		.packagesToScan("com.sante.patient.controller")
		.pathsToMatch("/patient/**")
		.build();
    }

}
