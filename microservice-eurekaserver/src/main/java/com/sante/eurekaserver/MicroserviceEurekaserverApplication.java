package com.sante.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author trimok
 *
 *
 *         Main class of the application
 */
@SpringBootApplication
@EnableEurekaServer
public class MicroserviceEurekaserverApplication {

    /**
     * Entry point of the application
     * 
     * @param args : arguments
     */
    public static void main(String[] args) {
	SpringApplication.run(MicroserviceEurekaserverApplication.class, args);
    }

}
