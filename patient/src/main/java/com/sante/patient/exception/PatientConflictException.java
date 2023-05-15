package com.sante.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ResponseStatus(HttpStatus.CONFLICT)
@Getter
@Setter
@NoArgsConstructor
public class PatientConflictException extends PatientRuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 3L;

    public PatientConflictException(String action) {
	super(action);
    }
}
