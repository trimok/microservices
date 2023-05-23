package com.sante.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         PatientRuntimeException, classe mère pour les exceptions Runtime de
 *         l'application
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientRuntimeException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 4L;
    private String action;
}
