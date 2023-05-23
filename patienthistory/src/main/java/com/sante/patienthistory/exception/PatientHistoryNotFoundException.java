package com.sante.patienthistory.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         PatientHistoryNotFoundException
 */
@Getter
@Setter
@NoArgsConstructor
public class PatientHistoryNotFoundException extends PatientHistoryRuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PatientHistoryNotFoundException(String action) {
	super(action);
    }

}
