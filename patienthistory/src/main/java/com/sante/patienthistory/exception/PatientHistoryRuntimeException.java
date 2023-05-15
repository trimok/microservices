package com.sante.patienthistory.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
