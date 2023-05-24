package com.sante.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         PatientNoContentException
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
@Getter
@Setter
@NoArgsConstructor
public class PatientNoContentException extends PatientRuntimeException {
    private static final long serialVersionUID = 2L;

    public PatientNoContentException(String action) {
	super(action);
    }
}
