package com.sante.patienthistory.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    private String message;
    private List<String> details = new ArrayList<>();

    public Error(String message, String action) {
	this.message = message;
	this.details.add(action);
    }
}
