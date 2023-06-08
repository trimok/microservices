package com.sante.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author trimok
 *
 *         Main clas of the application
 *
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    /**
     * Entry point of the application
     * 
     * @param args : arguments
     */
    public static void main(String[] args) {
	SpringApplication.run(ConfigServerApplication.class, args);
    }

}
