package com.sante.expert.exception;

import static com.sante.expert.constants.Constants.DECLENCHEUR_NOT_FOUND;
//import static com.sante.patient.constants.Constants.*;
import static com.sante.expert.constants.Constants.ERROR_VALIDATION;
import static com.sante.expert.constants.Constants.RISQUE_NOT_FOUND;

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
public class ExpertExceptionHandler {

    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class,
	    RisqueNotFoundException.class, DeclencheurNotFoundException.class
    })
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

	} else if (exception instanceof RisqueNotFoundException) {
	    RisqueNotFoundException pnfe = (RisqueNotFoundException) exception;
	    error = new Error(pnfe.getAction(), RISQUE_NOT_FOUND);
	    status = HttpStatus.NOT_FOUND;
	} else if (exception instanceof DeclencheurNotFoundException) {
	    DeclencheurNotFoundException dnfe = (DeclencheurNotFoundException) exception;
	    error = new Error(dnfe.getAction(), DECLENCHEUR_NOT_FOUND);
	    status = HttpStatus.NOT_FOUND;
	}
	return new ResponseEntity<Error>(error, status);
    }
}
