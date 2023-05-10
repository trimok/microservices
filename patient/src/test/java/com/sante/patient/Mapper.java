package com.sante.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mapper extends ObjectMapper {
    /**
     * 
     */
    private static final long serialVersionUID = 10L;

    /**
     * 
     */
    public Mapper() {
	// Management for the LocalDate serialization / deserialization
	this.registerModule(new JavaTimeModule());
    }
}
