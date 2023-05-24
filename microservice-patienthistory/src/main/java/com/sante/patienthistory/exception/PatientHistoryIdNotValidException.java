package com.sante.patienthistory.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         PatientHistoryIdNotValidException
 */
@Getter
@Setter
@NoArgsConstructor
public class PatientHistoryIdNotValidException extends PatientHistoryRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5L;

    public PatientHistoryIdNotValidException(String action) {
	super(action);
    }
}
