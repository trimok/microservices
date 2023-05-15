package com.sante.patienthistory.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
