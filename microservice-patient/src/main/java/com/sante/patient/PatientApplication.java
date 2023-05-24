package com.sante.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author trimok
 *
 *         Classe principale de l'application
 */
@SpringBootApplication
public class PatientApplication {

    /**
     * Point d'entrée de l'application
     * 
     * @param args : arguments
     */
    public static void main(String[] args) {
	SpringApplication.run(PatientApplication.class, args);
    }

}
