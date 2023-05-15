package com.sante.patient.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientNotFoundException extends PatientRuntimeException {
    public PatientNotFoundException(String action) {
	super(action);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
