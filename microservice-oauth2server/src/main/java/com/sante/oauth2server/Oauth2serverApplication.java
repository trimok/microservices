package com.sante.oauth2server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author trimok
 *
 *
 *         Classe principale de l'application
 */
@SpringBootApplication
public class Oauth2serverApplication {

    /**
     * Point d'entrée de l'application
     * 
     * @param args : arguments
     */
    public static void main(String[] args) {
	SpringApplication.run(Oauth2serverApplication.class, args);
    }
}
