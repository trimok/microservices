package com.sante.clientui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author trimok
 *
 *         Main class
 */
@SpringBootApplication
@EnableFeignClients("com.sante.clientui.service")
public class ClientuiApplication {

    /**
     * Entry point of the application
     * 
     * @param args : arguments
     */
    public static void main(String[] args) {
	SpringApplication.run(ClientuiApplication.class, args);
    }

}
