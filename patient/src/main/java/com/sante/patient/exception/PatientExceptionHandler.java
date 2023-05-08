package com.sante.patient.exception;

//import static com.sante.patient.constants.Constants.*;
import static com.sante.patient.constants.Constants.ERROR_VALIDATION;
import static com.sante.patient.constants.Constants.NOT_FOUND;
import static com.sante.patient.constants.Constants.NO_CONTENT;

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
public class PatientExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class,
	    PatientNoContentException.class,
	    PatientNotFoundException.class, })
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
	    error = new Error(ERROR_VALIDATION, "patient : date naissance :" + exception.getMessage());
	    status = HttpStatus.BAD_REQUEST;

	} else if (exception instanceof PatientNotFoundException) {
	    PatientNotFoundException pnfe = (PatientNotFoundException) exception;
	    error = new Error(pnfe.getAction(), NOT_FOUND);
	    status = HttpStatus.NOT_FOUND;
	} else if (exception instanceof PatientNoContentException) {
	    PatientNoContentException pnce = (PatientNoContentException) exception;
	    error = new Error(pnce.getAction(), NO_CONTENT);
	    status = HttpStatus.NO_CONTENT;
	}
	return new ResponseEntity<Error>(error, status);
    }
}
