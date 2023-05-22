package com.sante.expert.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeclencheurNotFoundException extends ExpertRuntimeException {
    public DeclencheurNotFoundException(String action) {
	super(action);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 2L;
}
