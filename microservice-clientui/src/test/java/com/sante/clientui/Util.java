package com.sante.clientui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sante.clientui.model.Patient;

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

    public static Patient getPatientFromMvcResult(MvcResult mvcResult)
	    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
	String json = mvcResult.getResponse().getContentAsString();
	Patient patient = mapper.readValue(json, Patient.class);
	return patient;
    }

    public static List<Patient> getListPatientFromMvcResult(MvcResult mvcResult)
	    throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
	String json = mvcResult.getResponse().getContentAsString();
	List<Patient> patients = new Mapper().readValue(json, new TypeReference<List<Patient>>() {
	});
	return patients;
    }

    public static String getTotalToken() {
	return "eyJraWQiOiI1ZjMzMmI0OS1mNDM1LTQwMjEtOTQ2NS1kZWI0ZWQxYTEwNzMiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0b3RhbCIsImF1ZCI6ImNsaWVudCIsIm5iZiI6MTY4Njc1OTAzOSwic2NvcGUiOlsib3BlbmlkIl0sInJvbGVzIjpbIlJPTEVfRVhQRVJUX1VTRVIiLCJST0xFX1BBVElFTlRfVVNFUiIsIlJPTEVfUEFUSUVOVF9ISVNUT1JZX1VTRVIiLCJST0xFX0dBVEVXQVlfVVNFUiJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwMDAiLCJleHAiOjE2OTUzOTkwMzksImlhdCI6MTY4Njc1OTAzOX0.BE1CAthd7aCpGK5GYT5NsiGvv20OGJxjdt6q38YfBSuTG_m8_VyOs3quENqgQdh213BlOQm3fwtnlVUCWieV67c0W6Jgxpk08WoInYTiEcTj_1R7iWiL8s08cj_T_eMeH9_OygbHqCRQaV0n8pRy3E5vJl4WNItA5N6Tg_ztg6xx-BOJfLM1LRtokWlnfTsbr1Tq5ZnymjW6l1BVXwVgnaVlQT8QP9OIDHq4hb2wxoyPqeVG9lviel09qSMfoCGG_uZ1N6s2p1bbEE6NFDLlBxl64uB8xoLCwXQ4aVKFvfYpD5mV9bF3ZrshBEgWDzOy7HOXQ7F3lhfPqMAT4HxAVA";
    }
}
