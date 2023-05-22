package com.sante.expert.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RisqueNotFoundException extends ExpertRuntimeException {
    public RisqueNotFoundException(String action) {
	super(action);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
}
