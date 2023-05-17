package com.sante.patienthistory.exception;

//import static com.sante.patienthistory.constants.Constants.*;
import static com.sante.patienthistory.constants.Constants.ERROR_VALIDATION;
import static com.sante.patienthistory.constants.Constants.NOT_FOUND;
import static com.sante.patienthistory.constants.Constants.NO_CONTENT;
import static com.sante.patienthistory.constants.Constants.PATIENT_ID_NOT_FOUND;
import static com.sante.patienthistory.constants.Constants.PATIENT_NOTE_DATE_NOT_VALID;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class PatientHistoryExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class,
	    PatientHistoryNoContentException.class,
	    PatientHistoryNotFoundException.class,
	    PatientHistoryIdNotValidException.class,
	    PatientNoteNotValidException.class })
    public ResponseEntity<Error> handlException(Exception exception, WebRequest request) {

	HttpStatus status = null;
	Error error = null;

	if (exception instanceof MethodArgumentNotValidException) {

	    List<String> details = new ArrayList<>();
	    for (ObjectError objectError : ((MethodArgumentNotValidException) exception).getBindingResult()
		    .getAllErrors()) {
		details.add(objectError.getObjectName() + " : "
			+ ((DefaultMessageSourceResolvable) (objectError.getArguments()[0])).getDefaultMessage()
			+ " : "
			+ objectError.getDefaultMessage());
	    }
	    error = new Error(ERROR_VALIDATION, details);
	    status = HttpStatus.BAD_REQUEST;
	} else if (exception instanceof HttpMessageNotReadableException) {
	    error = new Error(ERROR_VALIDATION, "info : date creation :" + exception.getMessage());
	    status = HttpStatus.BAD_REQUEST;

	} else if (exception instanceof PatientHistoryNotFoundException) {
	    PatientHistoryNotFoundException pnfe = (PatientHistoryNotFoundException) exception;
	    error = new Error(pnfe.getAction(), NOT_FOUND);
	    status = HttpStatus.NOT_FOUND;
	} else if (exception instanceof PatientHistoryNoContentException) {
	    PatientHistoryNoContentException pnce = (PatientHistoryNoContentException) exception;
	    error = new Error(pnce.getAction(), NO_CONTENT);
	    status = HttpStatus.NO_CONTENT;
	} else if (exception instanceof PatientHistoryIdNotValidException) {
	    PatientHistoryIdNotValidException pinve = (PatientHistoryIdNotValidException) exception;
	    error = new Error(pinve.getAction(), PATIENT_ID_NOT_FOUND);
	    status = HttpStatus.BAD_REQUEST;
	} else if (exception instanceof PatientNoteNotValidException) {
	    PatientNoteNotValidException pnve = (PatientNoteNotValidException) exception;
	    error = new Error(pnve.getAction(), PATIENT_NOTE_DATE_NOT_VALID);
	    status = HttpStatus.BAD_REQUEST;
	}
	return new ResponseEntity<Error>(error, status);
    }
}
