package com.sante.patienthistory.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Classe mère pour les exceptions Runtime de l'application
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientHistoryRuntimeException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4L;
    private String action;
}
