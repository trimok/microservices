package com.sante.patienthistory;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sante.patienthistory.model.PatientHistory;

public class Util {

    public static class Mapper extends ObjectMapper {
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

    public static Mapper mapper = new Mapper();

    public static PatientHistory getPatientHistoryFromMvcResult(MvcResult mvcResult)
	    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
	String json = mvcResult.getResponse().getContentAsString();
	PatientHistory patientHistory = mapper.readValue(json, PatientHistory.class);
	return patientHistory;
    }

    public static List<PatientHistory> getListPatientHistoryFromMvcResult(MvcResult mvcResult)
	    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
	String json = mvcResult.getResponse().getContentAsString();
	List<PatientHistory> patients = new Mapper().readValue(json, new TypeReference<List<PatientHistory>>() {
	});
	return patients;
    }
}
