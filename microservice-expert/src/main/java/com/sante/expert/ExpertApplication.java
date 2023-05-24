package com.sante.expert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author trimok
 *
 *         Classe principale de l'application
 *
 */

@SpringBootApplication
public class ExpertApplication {

    /**
     * Point d'entrée de l'application
     * 
     * @param args : arguments de l'application
     */
    public static void main(String[] args) {
	SpringApplication.run(ExpertApplication.class, args);
    }

}
