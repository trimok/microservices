package com.sante.patienthistory.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         PatientNoteNotValidException
 */
@Getter
@Setter
@NoArgsConstructor
public class PatientNoteNotValidException extends PatientHistoryRuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 6L;

    public PatientNoteNotValidException(String action) {
	super(action);
    }

}