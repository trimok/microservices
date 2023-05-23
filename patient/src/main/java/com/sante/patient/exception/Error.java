package com.sante.patient.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         L'objet error est renvoyé en cas de levée d'exception
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    /**
     * message principale
     */
    private String message;
    /**
     * informations
     */
    private List<String> details = new ArrayList<>();

    public Error(String message, String action) {
	this.message = message;
	this.details.add(action);
    }
}
