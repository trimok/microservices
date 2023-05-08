package com.sante.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ResponseStatus(HttpStatus.NO_CONTENT)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientNoContentException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 2L;
    private String action;
}
