package com.sante.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.WebSession;

import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class MicroserviceGatewayApplication {

    public static void main(String[] args) {
	SpringApplication.run(MicroserviceGatewayApplication.class, args);
    }

    @GetMapping("/")
    public Mono<String> index(WebSession session) {
	return Mono.just(session.getId());
    }
}
