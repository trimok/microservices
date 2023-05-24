package com.sante.expert.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Objet renvoyé si une exception est levée
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    /**
     * Le message
     */
    private String message;
    /**
     * Les informations
     */
    private List<String> details = new ArrayList<>();

    /**
     * @param message : message
     * @param action  : action (CRUD)
     */
    public Error(String message, String action) {
	this.message = message;
	this.details.add(action);
    }
}
