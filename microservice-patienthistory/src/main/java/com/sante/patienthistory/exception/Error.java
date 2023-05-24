package com.sante.patienthistory.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         La classe Error renvoyée lors de la levée d'une exception
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    /**
     * message principal
     */
    private String message;
    /**
     * details
     */
    private List<String> details = new ArrayList<>();

    public Error(String message, String action) {
	this.message = message;
	this.details.add(action);
    }
}
