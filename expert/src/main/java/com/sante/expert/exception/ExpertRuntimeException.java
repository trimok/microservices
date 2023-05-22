package com.sante.expert.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ResponseStatus(HttpStatus.NO_CONTENT)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpertRuntimeException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 4L;
    private String action;
}
