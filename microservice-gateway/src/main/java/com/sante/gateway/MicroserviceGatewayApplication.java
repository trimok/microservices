package com.sante.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author trimok
 *
 *         Main class
 *
 */
@SpringBootApplication
@RestController
public class MicroserviceGatewayApplication {

    /**
     * Entry point of the application
     * 
     * @param args : arguments
     */
    public static void main(String[] args) {
	SpringApplication.run(MicroserviceGatewayApplication.class, args);
    }
}
