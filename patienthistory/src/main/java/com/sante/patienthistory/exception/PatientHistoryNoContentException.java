package com.sante.patienthistory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ResponseStatus(HttpStatus.NO_CONTENT)
@Getter
@Setter
@NoArgsConstructor
public class PatientHistoryNoContentException extends PatientHistoryRuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 2L;

    public PatientHistoryNoContentException(String action) {
	super(action);
    }
}
